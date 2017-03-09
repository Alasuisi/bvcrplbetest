package testclient;

import java.awt.geom.Point2D;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
		//crunchifyClient.getCtoFResponse();
		//crunchifyClient.getFtoCResponse();
		
		test.testTransferInsert(100);
		//test.testGetTransfer(89);
	}
	private void testGetTransfer(int id)
		{
		System.out.println("\n============ testing getTransfers============");
		Client client = Client.create();
		String userid=new Integer(id).toString();
		WebResource resource = client.resource("http://localhost:8080/bvcrplbe/OfferRide/"+userid);
		//WebResource resource = client.resource("http://82.223.67.189:8080/bvcrplbe/offertran/"+userid);
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
			String from = "largo colli albani roma";
			String to ="via deruta roma";
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
			LinkedList<TimedPoint2D> pathpp = new LinkedList<TimedPoint2D>();
			for(int i=0;i<steps.length;i++)
				{
				 double lat = steps[i].startLocation.lat;
				 double lng = steps[i].startLocation.lng;
				 //TravelMode travel =TravelMode.DRIVING;
				 Duration duration =steps[i].duration;
				 //Double travelTime = new Double(duration.inSeconds);
				 //Point2D.Double coord = new Point2D.Double(lat,lng);
				 TimedPoint2D toAdd = new TimedPoint2D(lat,lng,duration.inSeconds);
				 pathpp.add(toAdd);
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
			testTran.setDep_time(System.currentTimeMillis());
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
			
			ObjectMapper mapper = new ObjectMapper();
			String jsonTran = mapper.writeValueAsString(testTran);
			
			Client client = Client.create();
			WebResource resource = client.resource("http://localhost:8080/bvcrplbe/OfferRide");
			//WebResource resource = client.resource("http://82.223.67.189:8080/bvcrplbe/offertran");
			ClientResponse response = resource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class,jsonTran);
			if (response.getStatus() != 201) {
				throw new RuntimeException("Failed : HTTP error code : "
				     + response.getStatus()+" "+response.getEntity(String.class));
			}
			System.out.println("Output from Server .... \n");
			String output = response.getEntity(String.class);
			System.out.println(output);
		}
 
	

}
