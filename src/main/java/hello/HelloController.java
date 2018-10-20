package hello;

import org.springframework.web.bind.annotation.RestController;

import com.github.fge.jsonschema.core.exceptions.ProcessingException;


import org.json.simple.JSONObject;
import redis.clients.jedis.Jedis;
import validator.JsonValidation;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.json.simple.parser.JSONParser;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.json.BasicJsonParser;
import org.springframework.boot.json.JsonParser;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@RestController
public class HelloController{
	Jedis jedis = new Jedis();
	JSONParser jsonParser = new JSONParser();
    @RequestMapping("/")
    public String index() {
        return "Greetings from Spring Boot2!";
    }
    
    @RequestMapping(value="/add", method=RequestMethod.POST)
    public String addPlan(@RequestBody JSONObject usecase, HttpServletResponse response) throws IOException, ProcessingException
    {
    	String data = usecase.toString();
    	Boolean jsonValidity = JsonValidation.isJSONValid(data);
    	if(jsonValidity == true) {
    	String uuid = UUID.randomUUID().toString();
    	String key = "plan_"+uuid;
    	jedis.set(key,data);
    	return "successfully added " + key;
    	}
    	else {
    		response.setStatus(400);
			return "JSON schema validation failed";
    	}
    }
    
    @RequestMapping(value="/getPlan/{plan_id}", method=RequestMethod.GET)
    public String getPlan(@PathVariable String plan_id, HttpServletResponse response)
    {
    	jedis.connect();
    	String key = plan_id;
    	Set<String> plankey = jedis.keys(key);
    	if (plankey.isEmpty()) {
			response.setStatus(404);
			return "Plan does not exist!!";
		}
    	String plan =  jedis.get(key);
    	return plan;
    }
    
    @RequestMapping(value="/delete/{plan_id}", method=RequestMethod.DELETE)
    public String delPlan(@PathVariable String plan_id, HttpServletResponse response)
    {
    	jedis.connect();
    	String key = plan_id;
    	Set<String> plankey = jedis.keys(key);
    	if (plankey.isEmpty()) {
			response.setStatus(404);
			return "Plan does not exist!!";
		}
    	jedis.del(key);
    	return key + " successfully deleted";
    }
    
    @RequestMapping(value="/update/{plan_id}", method=RequestMethod.PUT)
    public String updatePlan(@PathVariable String plan_id, @RequestBody JSONObject usecase, HttpServletResponse response) throws IOException, ProcessingException
    {
    	String data = usecase.toString();
    	Boolean jsonValidity = JsonValidation.isJSONValid(data);
    	if(jsonValidity == true) {
    	jedis.set(plan_id,data);
    	return "successfully added " + plan_id;
    	}
    	else {
    		response.setStatus(400);
			return "JSON schema validation failed";
    	}
    }
}
