// Created by plusminus on 00:23:14 - 03.10.2008
package ru.stilsoft.treasuremount.map;

import android.app.Fragment;
import android.app.FragmentManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import org.osmdroid.ResourceProxy;
import org.osmdroid.tileprovider.MapTileProviderArray;
import org.osmdroid.tileprovider.modules.IArchiveFile;
import org.osmdroid.tileprovider.modules.MBTilesFileArchive;
import org.osmdroid.tileprovider.modules.MapTileFileArchiveProvider;
import org.osmdroid.tileprovider.modules.MapTileModuleProviderBase;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.tileprovider.util.SimpleRegisterReceiver;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.MinimapOverlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import ru.stilsoft.treasuremount.R;
import ru.stilsoft.treasuremount.databasesupport.DatabaseInitializer;
import ru.stilsoft.treasuremount.databasesupport.DatabaseSupporter;
import ru.stilsoft.treasuremount.model.Location;
import ru.stilsoft.treasuremount.model.Treasure;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MapFragment extends Fragment implements OpenStreetMapConstants
{
    public static final int LOCATION_OPEN_TIMEOUT = 1; // min
    public static final int LOCATION_OPEN_RADIUS = 50; // m

    // ===========================================================
    // Constants
    // ===========================================================

    private static final int DIALOG_ABOUT_ID = 1;

    private static final int MENU_SAMPLES = Menu.FIRST + 1;
    private static final int MENU_ABOUT = MENU_SAMPLES + 1;

    private static final int MENU_LAST_ID = MENU_ABOUT + 1; // Always set to last unused id

    // ===========================================================
    // Fields
    // ===========================================================

    private SharedPreferences mPrefs;
    private MapView mMapView;
    private MyLocationNewOverlay mLocationOverlay;
    private CompassOverlay mCompassOverlay;
    private MinimapOverlay mMinimapOverlay;
	private ScaleBarOverlay mScaleBarOverlay;
    private RotationGestureOverlay mRotationGestureOverlay;
    private ResourceProxy mResourceProxy;

    private List<Location> mLocations = new ArrayList<>();
    private List<Location> mAllObjects = new ArrayList<>();
    private Map<Location, List<Treasure>> treasureMap = new HashMap<>();

    ArrayList<OverlayItem> mOverlayItemArray = new ArrayList<>();

    ItemizedIconOverlay.OnItemGestureListener<OverlayItem> mMyOnItemGestureListener = new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
        @Override
        public boolean onItemSingleTapUp(int index, OverlayItem item) {
            return false;
        }

        @Override
        public boolean onItemLongPress(int index, OverlayItem item) {
            return false;
        }
    };

    protected Drawable mLocationNewDraw;
    protected Drawable mLocationOpenDraw;
    protected Drawable mLocationFinishedDraw;

    protected Drawable mTreasureMoneyDraw;
    protected Drawable mTreasureEyeDraw;
    protected Drawable mTreasureTimeDraw;

    protected ScheduledExecutorService mExecutorService;
    protected Future mCheckMyLocationFuture;

	public static MapFragment newInstance() {
		MapFragment fragment = new MapFragment();
		return fragment;
	}

    @Override
    public void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)  {
        mResourceProxy = new ResourceProxyImpl(inflater.getContext().getApplicationContext());


		String[] urls = {"http://127.0.0.1"};
		XYTileSource MBTILESRENDER = new XYTileSource("mbtiles",
				ResourceProxy.string.offline_mode,
				11,
				15,
				256,
				".png",
				urls);

        File f = new File(Environment.getExternalStorageDirectory(), "/TreasureMount/" + "map.mbtiles");

        IArchiveFile[] mapFiles = new IArchiveFile[] { MBTilesFileArchive.getDatabaseFileArchive(f) };

        SimpleRegisterReceiver simpleReceiver = new SimpleRegisterReceiver(inflater.getContext());
        MapTileModuleProviderBase moduleProvider = new MapTileFileArchiveProvider(simpleReceiver, MBTILESRENDER, mapFiles);
        MapTileProviderArray provider = new MapTileProviderArray(MBTILESRENDER, null, new MapTileModuleProviderBase[] { moduleProvider });

        mMapView = new MapView(inflater.getContext(), 256, mResourceProxy, provider);
		mMapView.setTileSource(MBTILESRENDER);
		mMapView.setUseDataConnection(false);

		// Call this method to turn off hardware acceleration at the View level.
        // setHardwareAccelerationOff();
        return mMapView;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setHardwareAccelerationOff()
    {
        // Turn off hardware acceleration here, or in manifest
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            mMapView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        final Context context = this.getActivity();
		final DisplayMetrics dm = context.getResources().getDisplayMetrics();
        // mResourceProxy = new ResourceProxyImpl(getActivity().getApplicationContext());

        mPrefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        this.mCompassOverlay = new CompassOverlay(context, new InternalCompassOrientationProvider(context),
                mMapView);
        this.mLocationOverlay = new MyLocationNewOverlay(context, new GpsMyLocationProvider(context), mMapView);

        //mMinimapOverlay = new MinimapOverlay(context, mMapView.getTileRequestCompleteHandler());
		//mMinimapOverlay.setWidth(dm.widthPixels / 5);
		//mMinimapOverlay.setHeight(dm.heightPixels / 5);

		mScaleBarOverlay = new ScaleBarOverlay(context);
		mScaleBarOverlay.setCentred(true);
		mScaleBarOverlay.setScaleBarOffset(dm.widthPixels / 2, 10);

        mRotationGestureOverlay = new RotationGestureOverlay(context, mMapView);
		mRotationGestureOverlay.setEnabled(false);

        mMapView.setBuiltInZoomControls(true);
        mMapView.setMultiTouchControls(true);
        mMapView.getOverlays().add(this.mLocationOverlay);
        mMapView.getOverlays().add(this.mCompassOverlay);
        //mMapView.getOverlays().add(this.mMinimapOverlay);
		mMapView.getOverlays().add(this.mScaleBarOverlay);
        mMapView.getOverlays().add(this.mRotationGestureOverlay);

        mMapView.getController().setZoom(mPrefs.getInt(PREFS_ZOOM_LEVEL, 13));
        mMapView.scrollTo(mPrefs.getInt(PREFS_SCROLL_X, 0), mPrefs.getInt(PREFS_SCROLL_Y, 0));

		mLocationOverlay.enableMyLocation();
        mLocationOverlay.setDrawAccuracyEnabled(true);
		mCompassOverlay.enableCompass();

        mLocationNewDraw = context.getResources().getDrawable(R.drawable.location_new);
        mLocationOpenDraw = context.getResources().getDrawable(R.drawable.location_open);
        mLocationFinishedDraw = context.getResources().getDrawable(R.drawable.location_finished);

        mTreasureMoneyDraw = context.getResources().getDrawable(R.drawable.treasure_money);
        mTreasureEyeDraw = context.getResources().getDrawable(R.drawable.treasure_eye);
        mTreasureTimeDraw = context.getResources().getDrawable(R.drawable.treasure_time);

        List<Location> locations = DatabaseSupporter.getMainLocations();
        mLocations.addAll(locations);
        mAllObjects.addAll(locations);

        for (Location location : locations) {
            List<Treasure> treasures = DatabaseSupporter.getTreasuresByMainLocation(location);
            treasureMap.put(location, treasures);
            mAllObjects.addAll(treasures);
        }

        for (Location location : mAllObjects) {
            mOverlayItemArray.add(new OverlayItem("", "Russia", new GeoPoint(location.getLatitude(), location.getLongitude())));
        }


        ItemizedIconOverlay<OverlayItem> anotherItemizedIconOverlay = new ItemizedIconOverlay<OverlayItem>(context, mOverlayItemArray, mMyOnItemGestureListener) {
            @Override
            public void draw(Canvas canvas, MapView mapview, boolean arg2) {
                for (int i = 0; i < mOverlayItemArray.size(); ++i) {
                    GeoPoint in = mOverlayItemArray.get(i).getPoint();

                    Point out = new Point();
                    mapview.getProjection().toPixels(in, out);

                    Location location = mAllObjects.get(i);
                    if (location instanceof Treasure && location.getState() == Location.LOCATION_STATE_OPEN) {
                        drawObject((Treasure) location, canvas, out.x, out.y);
                    } else {
                        drawObject(location, canvas, out.x, out.y);
                    }
                }
            }
        };

        mMapView.getOverlays().add(anotherItemizedIconOverlay);

        setHasOptionsMenu(true);
    }

    @Override
    public void onResume()
    {
        super.onResume();
		/*
        final String tileSourceName = mPrefs.getString(PREFS_TILE_SOURCE,
                TileSourceFactory.DEFAULT_TILE_SOURCE.name());
        try {
            final ITileSource tileSource = TileSourceFactory.getTileSource(tileSourceName);
            mMapView.setTileSource(tileSource);
        } catch (final IllegalArgumentException e) {
            mMapView.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);
        }
        */

        if (mPrefs.getBoolean(PREFS_SHOW_LOCATION, false)) {
            this.mLocationOverlay.enableMyLocation();
            this.mLocationOverlay.enableFollowLocation();
        }
        if (mPrefs.getBoolean(PREFS_SHOW_COMPASS, false)) {
            this.mCompassOverlay.enableCompass();
        }

        mExecutorService = Executors.newSingleThreadScheduledExecutor();
        mCheckMyLocationFuture = mExecutorService.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                SQLiteDatabase sqLiteDatabase = DatabaseInitializer.sqLiteDatabase;
                boolean updateMapView = false;
                GeoPoint myLocation = mLocationOverlay.getMyLocation();
                if (myLocation != null) {
                    for (Location location : mLocations) {
                        if (location.getState() == Location.LOCATION_STATE_NEW && myLocation.distanceTo(location) <= LOCATION_OPEN_RADIUS) {
                            try {
                                sqLiteDatabase.beginTransaction();
                                updateMapView = true;
                                location.setState(Location.LOCATION_STATE_OPEN);
                                location.setLastChangedTime(System.currentTimeMillis());
                                DatabaseSupporter.updateMainLocationInDatabase(location);

                                List<Treasure> treasures = treasureMap.get(location);
                                for (Treasure treasure : treasures) {
                                    treasure.setState(Treasure.LOCATION_STATE_OPEN);
                                    DatabaseSupporter.updateTreasureInDatabase(treasure);
                                }

                                sqLiteDatabase.setTransactionSuccessful();
                            } finally {
                                sqLiteDatabase.endTransaction();
                            }
                        }
                    }

                    if (updateMapView)
                        mMapView.postInvalidate();
                }

                updateMapView = false;
                for (Location location : mLocations) {
                    if (location.getState() == Location.LOCATION_STATE_OPEN && ((System.currentTimeMillis() - location.getLastChangedTime()) / 60000) >= LOCATION_OPEN_TIMEOUT) {
                        try {
                            sqLiteDatabase.beginTransaction();
                            updateMapView = true;
                            location.setState(Location.LOCATION_STATE_FINISHED);
                            location.setLastChangedTime(System.currentTimeMillis());

                            List<Treasure> treasures = treasureMap.get(location);
                            for (Treasure treasure : treasures) {
                                treasure.setState(Treasure.LOCATION_STATE_FINISHED);
                                DatabaseSupporter.updateTreasureInDatabase(treasure);
                            }

                            sqLiteDatabase.setTransactionSuccessful();
                        } finally {
                            sqLiteDatabase.endTransaction();
                        }
                    }
                }

                if (updateMapView)
                    mMapView.postInvalidate();
            }
        }, 5, 5, TimeUnit.SECONDS);
    }

	@Override
	public void onPause() {
        mCheckMyLocationFuture.cancel(true);
        mExecutorService.shutdown();
        try {
            if (!mExecutorService.awaitTermination(3, TimeUnit.SECONDS)) {
                mExecutorService.shutdownNow();
                if (!mExecutorService.awaitTermination(3, TimeUnit.SECONDS))
                    System.err.println("Pool did not terminate");
            }
        } catch (InterruptedException ie) {
            mExecutorService.shutdownNow();
            Thread.currentThread().interrupt();
        }

        final SharedPreferences.Editor edit = mPrefs.edit();
        edit.putString(PREFS_TILE_SOURCE, mMapView.getTileProvider().getTileSource().name());
        edit.putInt(PREFS_SCROLL_X, mMapView.getScrollX());
        edit.putInt(PREFS_SCROLL_Y, mMapView.getScrollY());
        edit.putInt(PREFS_ZOOM_LEVEL, mMapView.getZoomLevel());
        edit.putBoolean(PREFS_SHOW_LOCATION, mLocationOverlay.isMyLocationEnabled());
        edit.putBoolean(PREFS_SHOW_COMPASS, mCompassOverlay.isCompassEnabled());
        edit.commit();

        this.mLocationOverlay.disableMyLocation();
        this.mLocationOverlay.disableFollowLocation();
        this.mCompassOverlay.disableCompass();

        super.onPause();
    }

    /*
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        // Put overlay items first
        mMapView.getOverlayManager().onCreateOptionsMenu(menu, MENU_LAST_ID, mMapView);

        // Put samples next
		SubMenu samplesSubMenu = menu.addSubMenu(0, MENU_SAMPLES, Menu.NONE, R.string.samples)
				.setIcon(android.R.drawable.ic_menu_gallery);
		SampleFactory sampleFactory = SampleFactory.getInstance();
		for (int a = 0; a < sampleFactory.count(); a++) {
			final BaseSampleFragment f = sampleFactory.getSample(a);
			samplesSubMenu.add(f.getSampleTitle()).setOnMenuItemClickListener(
					new OnMenuItemClickListener() {
						@Override
						public boolean onMenuItemClick(MenuItem item) {
							startSampleFragment(f);
							return true;
						}
					});
		}

        // Put "About" menu item last
        menu.add(0, MENU_ABOUT, Menu.CATEGORY_SECONDARY, R.string.about).setIcon(
                android.R.drawable.ic_menu_info_details);

       super.onCreateOptionsMenu(menu, inflater);
    }
    */

	protected void startSampleFragment(Fragment fragment) {
		FragmentManager fm = getFragmentManager();
		fm.beginTransaction().hide(this).add(android.R.id.content, fragment, "SampleFragment")
				.addToBackStack(null).commit();
	}

    /*
    @Override
    public void onPrepareOptionsMenu(final Menu pMenu)
    {
        mMapView.getOverlayManager().onPrepareOptionsMenu(pMenu, MENU_LAST_ID, mMapView);
        super.onPrepareOptionsMenu(pMenu);
    }
    */

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (mMapView.getOverlayManager().onOptionsItemSelected(item, MENU_LAST_ID, mMapView))
			return true;

		switch (item.getItemId()) {
		case MENU_ABOUT:
			getActivity().showDialog(DIALOG_ABOUT_ID);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public MapView getMapView() {
		return mMapView;
	}

    public void drawObject(Treasure treasure, Canvas canvas, int x, int y) {
        switch (treasure.getType()) {
            case Treasure.TREASURE_TYPE_MONEY:
                mLocationNewDraw.setBounds(x - 20, y - 20, x + 20, y + 20);
                mLocationNewDraw.draw(canvas);
                break;
            case Treasure.TREASURE_TYPE_EYE:
                mLocationOpenDraw.setBounds(x - 20, y - 20, x + 20, y + 20);
                mLocationOpenDraw.draw(canvas);
                break;
            case Treasure.TREASURE_TYPE_TIME:
                mLocationFinishedDraw.setBounds(x - 20, y - 20, x + 20, y + 20);
                mLocationFinishedDraw.draw(canvas);
                break;
        }
    }


    public void drawObject(Location location, Canvas canvas, int x, int y) {
        switch (location.getState()) {
            case Location.LOCATION_STATE_NEW:
                mLocationNewDraw.setBounds(x - 20, y - 20, x + 20, y + 20);
                mLocationNewDraw.draw(canvas);
                break;
            case Location.LOCATION_STATE_OPEN:
                mLocationOpenDraw.setBounds(x - 20, y - 20, x + 20, y + 20);
                mLocationOpenDraw.draw(canvas);
                break;
            case Location.LOCATION_STATE_FINISHED:
                mLocationFinishedDraw.setBounds(x - 20, y - 20, x + 20, y + 20);
                mLocationFinishedDraw.draw(canvas);
                break;
        }
    }
}
