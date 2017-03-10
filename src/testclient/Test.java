package testclient;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.core.MediaType;

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
		
		test.testTransferInsert(100);
		test.testGetTransfer(100);
	}
	private void testGetTransfer(int id)
		{
		System.out.println("\n============ testing getTransfers============");
		Client client = Client.create();
		String userid=new Integer(id).toString();
		//WebResource resource = client.resource("http://localhost:8080/bvcrplbe/OfferRide/"+userid);
		WebResource resource = client.resource("http://82.223.67.189:8080/carpoolingbe/OfferRide/"+userid);
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
			String from = "via ostiense roma";
			String to ="piazza re di roma roma";
			Calendar myCal = Calendar.getInstance();
			myCal.set(Calendar.YEAR, 2017);
			myCal.set(Calendar.MONTH, 12);
			myCal.set(Calendar.DAY_OF_MONTH, 25);
			myCal.set(Calendar.HOUR_OF_DAY,12);
			myCal.set(Calendar.MINUTE,30);
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
			System.out.println("durata della tratta (leg)"+lines.duration.inSeconds);
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
			long counterduration=0;
			LinkedList<TimedPoint2D> pathpp = new LinkedList<TimedPoint2D>();
			for(int i=0;i<steps.length;i++)
				{
				//System.out.println(steps[i].toString());
				 double lat = steps[i].startLocation.lat;
				 double lng = steps[i].startLocation.lng;
				 Duration duration =steps[i].duration;
				 counterduration=counterduration+duration.inSeconds;
				 long touchTime = theDate.getTime()+(duration.inSeconds*1000);
				 TimedPoint2D toAdd = new TimedPoint2D(lat,lng,touchTime);
				 pathpp.add(toAdd);
				 //System.out.println(toAdd.toString());
				 //System.out.println("durata somma degli step "+counterduration);
				}
			
			
			Transfer testTran = new Transfer();
			testTran.setAnimal(true);
			testTran.setArr_addr(to);
			Point2D.Double arrpoint = new Point2D.Double();
			arrpoint.setLocation(steps[steps.length-1].endLocation.lat, steps[steps.length-1].endLocation.lng);
			//arrpoint.setLocation(path.get(path.size()-1).lat,path.get(path.size()-1).lng);
			testTran.setArr_gps(arrpoint);
			testTran.setAva_seats(4);
			testTran.setClass_id(6);
			testTran.setDep_addr(from);
			Point2D.Double deppoint = new Point2D.Double();
			deppoint.setLocation(steps[0].startLocation.lat, steps[0].startLocation.lng);
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
			System.out.println(testTran);
			/*
			Iterator<TimedPoint2D> iter = testTran.getPath().iterator();
			while(iter.hasNext())
				{
				TimedPoint2D temp=iter.next();
				System.out.println(temp.toString()+" "+temp.getLatitude()+" "+temp.getLongitude());

				}*/
			
			ObjectMapper mapper = new ObjectMapper();
			
			
			/*String listapunti = mapper.writeValueAsString(testTran.getPath());
			System.out.println("json lista punti "+listapunti);

			CollectionType typeReference =TypeFactory.defaultInstance().constructCollectionType(LinkedList.class, TimedPoint2D.class);
				try {
					LinkedList<TimedPoint2D> resultDto = mapper.readValue(listapunti, typeReference);
					Iterator<TimedPoint2D> iter2 = resultDto.iterator();
					while(iter2.hasNext())
						{
						System.out.println("DIO MALEFICO "+iter2.next().toString());
						}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}*/
			
			
			//mapper.configure(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, true);
			String jsonTran = mapper.writeValueAsString(testTran);
			/*System.out.println("Transfer in json "+jsonTran);
			try {
				Transfer testDeddio = mapper.configure(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, true).readValue(jsonTran, Transfer.class);
				
				System.out.println("PORTANNA LA MADONNA CAGNA "+testDeddio);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			
			
			Client client = Client.create();
			//WebResource resource = client.resource("http://localhost:8080/bvcrplbe/OfferRide");
			WebResource resource = client.resource("http://82.223.67.189:8080/carpoolingbe/OfferRide");
			ClientResponse response = resource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class,jsonTran);
			if (response.getStatus() != 201) {
				throw new RuntimeException("Failed : HTTP error code : "
				     + response.getStatus()+" "+response.getEntity(String.class));
			}
			System.out.println("Output from Server .... \n");
			String output = response.getEntity(String.class);
			System.out.println("output "+output);
		}
 
	

}
