package testclient;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import javax.ws.rs.core.MediaType;

import org.gavaghan.geodesy.Ellipsoid;
import org.gavaghan.geodesy.GeodeticCalculator;
import org.gavaghan.geodesy.GlobalPosition;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.Duration;
import com.google.maps.model.EncodedPolyline;
import com.google.maps.model.LatLng;
import com.google.maps.model.TravelMode;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.ClientResponse.Status;


import com.sun.jersey.api.client.WebResource;



public class Test {
	public static void main(String[] args) throws JsonProcessingException {
		 
		Test test = new Test();
		
		//test.testTransferInsert(95);
		//test.testGetTransfer(101);
		test.testCSAnew(100,118,Long.MAX_VALUE,20);
		//test.testCSA();
		//test.testPool();
		//test.testGetSolutions(100, 118);
		//test.testUUID();
		//test.populate_pool();
		//test.testBookRide(100, 118, 6, "http://localhost:8080/testCallback/callback/driver/delete/");
		//test.testDeleteRide(90, 143);
	}
	
	private void testUUID()
		{
		for(int i=0;i<=10;i++)
			{
				UUID prova =UUID.randomUUID();
				System.out.println(prova);
			}
		}
	
	private void testDeleteRide(int userid,int tranid)
		{
		
		/*Client client = Client.create();
		String address = "http://localhost:8080/testCallback/callback/driver/delete";
		WebResource resource = client.resource(address);
		ClientResponse response = resource.type(MediaType.TEXT_PLAIN).post(ClientResponse.class, "Portanna");
		if(response.getStatus()!=200)
			{
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatus()+System.lineSeparator()+response.getEntity(String.class));
			}else System.out.println("SUCCESS: "+response.getEntity(String.class));*/
		
		Client client = Client.create();
		String address = "http://localhost:8080/bvcrplbe/OfferRide/"+userid+"/"+tranid+"/debug";
		WebResource resource = client.resource(address);
		ClientResponse response = resource.delete(ClientResponse.class);
		if(response.getStatus()!=200)
			{
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatus()+System.lineSeparator()+response.getEntity(String.class));
			}else System.out.println("SUCCESS: "+response.getEntity(String.class));
		
		}
	
	private  void testBookRide(int userid, int tranid,int solid,String callBack)
		{
		 Client client = Client.create();
		 String address="http://localhost:8080/bvcrplbe/BookRide/"+userid+"/"+tranid+"/"+solid;
		 WebResource resource = client.resource(address);
		 ClientResponse response = resource.type(MediaType.TEXT_PLAIN).get(ClientResponse.class);
		 if(response.getStatus()!=200)
		 	{
			 //System.out.println(response2.getEntity(String.class));
			 throw new RuntimeException("Failed : HTTP error code : " + response.getStatus()+System.lineSeparator()+response.getEntity(String.class));
		 	}else
		 		{
		 		 System.out.println(response.getEntity(String.class));
		 		}		 
		
		}
	
	private void populate_pool()
	{
	 Client client = Client.create();
	 String address= "http://localhost:8080/bvcrplbe/debugging/resetpool";
	 WebResource resource = client.resource(address);
	 ClientResponse response = resource.accept(MediaType.TEXT_PLAIN).get(ClientResponse.class);
	 if(response.getStatus()!=200)
	 	{
		 throw new RuntimeException("Failed : HTTP error code : " + response.getStatus()+System.lineSeparator()+response.getEntity(String.class));
	 	}else
	 		{
	 		String output = response.getEntity(String.class);
	 		System.out.println("SUCCESS");
			System.out.println(output);
	 		}
	}
	
	private void testGetSolutions(int userid, int tranid)
		{
		Client client = Client.create();
		String address = "http://localhost:8080/bvcrplbe/SearchRide/"+userid+"/"+tranid;
		 WebResource resource = client.resource(address);
		 ClientResponse response = resource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
		 if(response.getStatus()!=200){
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatus()+response.getEntity(String.class));
			}else
				{
				String output = response.getEntity(String.class);
				System.out.println(output);
				}
		}
	private void testPool()
		{
		Client client = Client.create();
		WebResource resource = client.resource("http://localhost:8080/bvcrplbe/OfferRide/pool/"+101+"/"+123);
		ClientResponse response = resource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
		if (response.getStatus() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
		}
		String output = response.getEntity(String.class);
		System.out.println(output);
		}
	
	private void testCSAnew(int userid,int transferID,long timeFrame,int limit) 
		{
		 Client client = Client.create();
		 String address = "http://localhost:8080/bvcrplbe/SearchRide/myrequest/"+userid+"/"+transferID;
		 WebResource resource = client.resource(address);
		 System.out.println(address);
		 ClientResponse response = resource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
		 if(response.getStatus()!= 200)
		 	{
			 throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
		 	}
		 client.destroy();
		 String transferString = response.getEntity(String.class);
		 System.out.println(transferString);
		 Client client2 = Client.create();
		 WebResource searchResource = client2.resource("http://localhost:8080/bvcrplbe/SearchRide/"+timeFrame+"/"+limit); //1800000
		 ClientResponse response2 = searchResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class,transferString);
		 if(response2.getStatus()!=200)
		 	{
			 //System.out.println(response2.getEntity(String.class));
			 throw new RuntimeException("Failed : HTTP error code : " + response2.getStatus());
		 	}else
		 		{
		 		 System.out.println(response2.getEntity(String.class));
		 		}
		}
	
	private void testCSA()
	{
	System.out.println("\n============ testing CSA============");
	Client client = Client.create();
	WebResource resource = client.resource("http://localhost:8080/bvcrplbe/OfferRide/CSA");
	//WebResource resource = client.resource("http://82.223.67.189:8080/carpoolingbe/OfferRide/"+userid);
	ClientResponse response = resource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
	if (response.getStatus() != 200) {
		throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
	}

	String output = response.getEntity(String.class);
	System.out.println(output);
	}
	
	
	
	private void testGetTransfer(int id)
		{
		System.out.println("\n============ testing getTransfers============");
		Client client = Client.create();
		String userid=new Integer(id).toString();
		WebResource resource = client.resource("http://localhost:8080/bvcrplbe/OfferRide/"+userid);
		//WebResource resource = client.resource("http://82.223.67.189:8080/bvcrplbe/OfferRide/"+userid);
		ClientResponse response = resource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
		if (response.getStatus() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
		}

		String output = response.getEntity(String.class);
		System.out.println("\n============getTransfers (user:"+userid+")============");
		System.out.println(output);
		}
	
	private  void testTransferInsert(int userid) throws JsonProcessingException
		{
			String from = "via appia nuova 119 roma";
			String to ="via merulana 121 roma";
			Calendar myCal = Calendar.getInstance();
			myCal.set(Calendar.YEAR, 2017);
			myCal.set(Calendar.MONTH, 11);
			myCal.set(Calendar.DAY_OF_MONTH, 25);
			myCal.set(Calendar.HOUR_OF_DAY,12);
			myCal.set(Calendar.MINUTE,48);
			Date theDate = myCal.getTime();
			GeoApiContext context = new GeoApiContext().setApiKey("AIzaSyBA-NgbRwnecHN3cApbnZoaCZH0ld66fT4");
			DirectionsResult results=null;
			try {
				results = DirectionsApi.getDirections(context, from, to).await();
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			DirectionsRoute[] routes = results.routes;
			DirectionsLeg lines=routes[0].legs[0];
			DirectionsStep[] steps = lines.steps;
			
			
			
			/*List<LatLng> path = lines.decodePath();
			LinkedList<TimedPoint2D> pathpp = new LinkedList<TimedPoint2D>();
			Iterator<LatLng> iter = path.iterator();
			while(iter.hasNext())
				{
				 LatLng temp =iter.next();
				 Point2D.Double coord = new Point2D.Double(temp.lat, temp.lng);
				 TimedPoint2D toAdd = new TimedPoint2D(coord,System.currentTimeMillis());
				 //toAdd.setLocation(temp.lat, temp.lng);
				 pathpp.add(toAdd);
				 
				}*/
			
			/*long counterduration=0;
			LinkedList<TimedPoint2D> pathpp = new LinkedList<TimedPoint2D>();
			double firstLat = steps[0].startLocation.lat;
			double firstlon = steps[0].startLocation.lng;
			System.out.println(firstLat+","+firstlon);
			long touchTime = theDate.getTime();
			TimedPoint2D source = new TimedPoint2D(firstLat,firstlon,touchTime);
			pathpp.add(source);
			for(int i=0;i<steps.length;i++)
				{
				//System.out.println(steps[i].toString());
				 double lat = steps[i].endLocation.lat;
				 double lng = steps[i].endLocation.lng;
				 System.out.println(lat+","+lng);
				 Duration duration =steps[i].duration;
				 counterduration=counterduration+duration.inSeconds;
				 //long touchTime = theDate.getTime()+(duration.inSeconds*1000);
				 touchTime = touchTime+(duration.inSeconds*1000);
				 TimedPoint2D toAdd = new TimedPoint2D(lat,lng,touchTime);
				 pathpp.add(toAdd);
				 //System.out.println(toAdd.toString());
				 //System.out.println("durata somma degli step "+counterduration);
				}*/
			LinkedList<TimedPoint2D> pathpp = new LinkedList<TimedPoint2D>();
			EncodedPolyline poli =routes[0].overviewPolyline;
			List<LatLng> poliList = poli.decodePath();
			Iterator<LatLng> polIter = poliList.iterator();
			NumberFormat nf = NumberFormat.getNumberInstance(Locale.UK);
			nf.setMaximumFractionDigits(5);    
			nf.setMinimumFractionDigits(5);
			nf.setGroupingUsed(false);
			LatLng previous = polIter.next();
			TimedPoint2D firstPoint= new TimedPoint2D();
			firstPoint.setLatitude(new Double(nf.format(previous.lat)));
			firstPoint.setLongitude(new Double(nf.format(previous.lng)));
			firstPoint.setTouchTime(theDate.getTime());
			pathpp.add(firstPoint);
			long ttime=theDate.getTime();
			double tdistance=0;
			
			while(polIter.hasNext())
				{
				LatLng actual = polIter.next();
				double distance = evaluateDistance(previous,actual);
				tdistance=tdistance+distance;
				long time = ((lines.duration.inSeconds*1000)/(lines.distance.inMeters))*Math.round(distance);
				//System.out.println(nf.format(previous.lat)+","+nf.format(previous.lng)+" --> "+nf.format(actual.lat)+","+nf.format(actual.lng)+" ttime: "+time);
				ttime=ttime+time;
				TimedPoint2D toAdd= new TimedPoint2D(new Double(nf.format(actual.lat)),new Double(nf.format(actual.lng)),ttime);
				pathpp.add(toAdd);
				previous=actual;
				}
			Iterator<TimedPoint2D> pathIter= pathpp.iterator();
			
			while(pathIter.hasNext())
				{
				System.out.println(pathIter.next());
				}
			System.out.println(System.lineSeparator()+"durata della tratta (leg) "+lines.duration.inSeconds*1000);
			System.out.println("Lunghezza della tratta "+lines.distance.inMeters);
			System.out.println("Distanza calcolata da me "+Math.round(tdistance));
			System.out.println("Printing path, total time:"+(ttime-theDate.getTime()));
			System.out.println(System.lineSeparator()+"Path polyline"+System.lineSeparator());
			Iterator<LatLng> polIter2 = poliList.iterator();
			while(polIter2.hasNext())
				{
				System.out.println(polIter2.next());
				}
			
			System.out.println("start point "+pathpp.getFirst()+" della leg"+steps[0].startLocation);
			System.out.println("end point "+pathpp.getLast()+" della leg"+steps[steps.length-1].endLocation);

			
			
			Transfer testTran = new Transfer();
			testTran.setAnimal(true);
			testTran.setArr_addr(to);
			Point2D.Double arrpoint = new Point2D.Double();
			arrpoint.setLocation(pathpp.getLast().getLatitude(), pathpp.getLast().getLongitude());
			//arrpoint.setLocation(path.get(path.size()-1).lat,path.get(path.size()-1).lng);
			testTran.setArr_gps(arrpoint);
			testTran.setAva_seats(4);
			testTran.setClass_id(6);
			testTran.setDep_addr(from);
			Point2D.Double deppoint = new Point2D.Double();
			deppoint.setLocation(pathpp.getFirst().getLatitude(),pathpp.getFirst().getLongitude());
			testTran.setDep_gps(deppoint);
			testTran.setDep_time(theDate.getTime());
			testTran.setHandicap(true);
			testTran.setLuggage(true);
			testTran.setOcc_seats(3);
			testTran.setPath(pathpp);
			testTran.setPool_id(666);
			testTran.setPrice(35);
			testTran.setProf_id(2);
			testTran.setReser_id(4);
			testTran.setSmoke(true);
			testTran.setStatus("booh");
			testTran.setType("tipo a caso");
			testTran.setUser_id(userid);
			testTran.setUser_role("driver");
			testTran.setDet_range(300);
			testTran.setRide_details("fiat panda del 75 turbo nafta");
			testTran.setCallback_uri("http\\prova.com");
			System.out.println(System.lineSeparator()+testTran);
			
			ObjectMapper mapper = new ObjectMapper();
			String jsonTran = mapper.writeValueAsString(testTran);
			
			
			Client client = Client.create();
			WebResource resource = client.resource("http://localhost:8080/bvcrplbe/OfferRide");
			//WebResource resource = client.resource("http://82.223.67.189:8080/carpoolingbe/OfferRide");
			ClientResponse response = resource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class,jsonTran);
			if (response.getStatus() != 201) {
				throw new RuntimeException("Failed : HTTP error code : "
				     + response.getStatus()+" "+response.getEntity(String.class));
			}
			System.out.println("Output from Server .... \n");
			String output = response.getEntity(String.class);
			System.out.println("output "+output);
		}
	
	
	private long travelTime(double distance)
	{
	 double meanSpeed = 1;
	 double timeSeconds = distance/meanSpeed;
	 double millitime =timeSeconds*1000;
	 if(millitime<0) System.err.println("che e'successo!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!?"+distance);
	 return Math.round(millitime);
	}
	
	private double evaluateDistance(LatLng previous,LatLng actual)
	{
	/*double dlon = pPoint.getLongitude()-dPoint.getLongitude();
	double dlat = pPoint.getLatitude()-dPoint.getLatitude();
	double a = Math.pow((Math.sin(dlat/2)),2) + Math.cos(dPoint.getLatitude());
	
			dlon = lon2 - lon1 
			dlat = lat2 - lat1 
			a = (sin(dlat/2))^2 + cos(lat1) * cos(lat2) * (sin(dlon/2))^2 
			c = 2 * atan2( sqrt(a), sqrt(1-a) ) 
			d = R * c (where R is the radius of the Earth)*/
	GeodeticCalculator geoCalc = new GeodeticCalculator();

	Ellipsoid reference = Ellipsoid.WGS84;  

	GlobalPosition pointA = new GlobalPosition(previous.lat, previous.lng, 0.0); // Point A

	GlobalPosition userPos = new GlobalPosition(actual.lat, actual.lng, 0.0); // Point B

	double distance = geoCalc.calculateGeodeticCurve(reference, userPos, pointA).getEllipsoidalDistance(); // Distance between Point A and Point B
	return distance;
	}
 
	

}
