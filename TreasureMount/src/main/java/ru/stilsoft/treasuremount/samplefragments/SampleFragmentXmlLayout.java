package ru.stilsoft.treasuremount.samplefragments;

import org.osmdroid.views.MapView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import ru.stilsoft.treasuremount.R;

public class SampleFragmentXmlLayout extends BaseSampleFragment {

	// ===========================================================
	// Fields
	// ===========================================================

	public static final String TITLE = "MapView in XML layout";

	@Override
	public String getSampleTitle() {
		return TITLE;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.mapview, null);
		mMapView = (MapView) v.findViewById(R.id.mapview);
		return v;
		// mResourceProxy = new ResourceProxyImpl(inflater.getContext().getApplicationContext());
		// mMapView = new MapView(inflater.getContext(), 256, mResourceProxy);
		// mMapView.setUseSafeCanvas(true);
		// return mMapView;
	}

}
