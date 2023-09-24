package crm.tranquil_sales_steer.data.requirements;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class LocationUtils
{
	String[] PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.CALL_PHONE};
	@NonNull
	public static Location getMyLocation(AppCompatActivity mActivity)
	{
		Location myLocation = new Location("");
		if((mActivity != null) && ! (mActivity.isFinishing()))
		{
			if(ActivityCompat.checkSelfPermission(mActivity.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mActivity.getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
			{
				myLocation = new Location("");
				ActivityCompat.requestPermissions(mActivity,new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},1);
			}
			
			else
			{
				GPSTracker1 gps = new GPSTracker1(mActivity);
				// Check if GPS enabled

					myLocation = new Location("");//provider name is unnecessary
					myLocation.setLatitude(gps.getLocation().getLatitude());//your coords of course
					myLocation.setLongitude(gps.getLocation().getLongitude());
					return myLocation;

			}
		}
		return myLocation;
	}
	
	public static String getAddress(double lat, double lng, AppCompatActivity mActivity)
	{
		String add = "";
		Geocoder geocoder = new Geocoder(mActivity, Locale.getDefault());
		try {
			List<Address> addresses = geocoder.getFromLocation(lat,lng,1);
			Address obj = addresses.get(0);
			add = obj.getAddressLine(0);
			add = add + "\n" + obj.getCountryName();
			add = add + "\n" + obj.getCountryCode();
			add = add + "\n" + obj.getAdminArea();
			add = add + "\n" + obj.getPostalCode();
			add = add + "\n" + obj.getSubAdminArea();
			add = add + "\n" + obj.getLocality();
			add = add + "\n" + obj.getSubThoroughfare();
			
			Log.v("IGA","Address" + add);
			// Toast.makeText(this, "Address=>" + add,
			// Toast.LENGTH_SHORT).show();
			
			// TennisAppActivity.showDialog(add);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Toast.makeText(mActivity,e.getMessage(), Toast.LENGTH_SHORT).show();
		}
		return add;
	}

}


