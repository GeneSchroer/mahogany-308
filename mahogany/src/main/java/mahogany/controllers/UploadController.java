package mahogany.controllers;

import java.io.IOException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.wololo.geojson.GeoJSON;
import org.wololo.geojson.Geometry;
import org.wololo.jts2geojson.GeoJSONReader;
import org.wololo.jts2geojson.GeoJSONWriter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.vividsolutions.jts.geom.MultiPolygon;

import mahogany.entities.Boundaries;
import mahogany.entities.Boundaries2;
import mahogany.entities.Districts;
import mahogany.repositories.Boundaries2Repository;
import mahogany.utils.FileUploadHelper;
import mahogany.utils.Source;
import mahogany.utils.UCLADistrictFileUploaderImpl;

@Controller
public class UploadController {

	@Autowired FileUploadHelper helper;
	
	@Autowired
	UCLADistrictFileUploaderImpl uclaDistrictConverter;
	
	@Autowired Boundaries2Repository boundaries2Repo ;
	@RequestMapping("/upload")
	public String uclaDistrictDataUploadRequest(@RequestParam("file")MultipartFile[] files) {
		//helper.setDistrictsRepository(districtsRepository);

		helper.uploadDistrictFiles(files, Source.UCLA);
		
		
		return "gerrymander";
	}
	
	@RequestMapping("/test")
	public String princetonElectionDataUploadRequest(@RequestParam("file")MultipartFile file) {
		
		helper.uploadElectionFile(file, Source.PRINCETON);
		
		return "gerrymander";
	}
	
	@RequestMapping("/boundaryTest")
	public String boundary2Request(@RequestParam("file")MultipartFile file) {
		ObjectMapper jsonNodeMapper = new ObjectMapper();
		ObjectReader jsonNodeReader = jsonNodeMapper.reader();
		
		System.out.println("Converting file: " + file.getOriginalFilename() );
		
		/* this line can throw an IOException*/
		String fileAsJsonString;
		try {
			fileAsJsonString = new String(file.getBytes());
			ObjectNode fileAsJsonNode = (ObjectNode)jsonNodeReader.readTree(fileAsJsonString);
			
			uclaDistrictConverter.uploadJsonToDatabase2(fileAsJsonNode);
			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		return "gerrymander";
	}
	
}
