package com.rediscache.example;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import redis.clients.jedis.Jedis;

@RestController
@SpringBootApplication
public class SpringBootRedisExampleApplication {

	private static Logger log = LoggerFactory.getLogger(SpringBootRedisExampleApplication.class);

	@Value("${redis.host}")
	private String redisHost;

	@Value("${redis.port}")
	private int redisPort;

	public static void main(String[] args) {
		SpringApplication.run(SpringBootRedisExampleApplication.class, args);
	}

	@GetMapping(value="/")
	public String pingRedis() {
		String response = null;
		log.info("In pingRedis redisHost="+redisHost+", redisPort="+redisPort);
		try {
			Jedis jedis = new Jedis(redisHost, redisPort);
			log.info("Connection to server sucessfully: " + redisHost); 
			//check whether server is running or not
			response = jedis.ping();
			log.info("Server is running: "+response); 
		} catch (Exception e) {
			response = e.getMessage();
			log.error("Error; "+e.getMessage(), e);
		}
		return response;
	}

	@GetMapping(value="/putInCache/{key}/{value}")
	public String putInCache(@PathVariable(name="key") String key, @PathVariable(name="value") String value) {
		String response = null;
		log.info("In putInCache redisHost="+redisHost+", redisPort="+redisPort);
		try {
			Jedis jedis = new Jedis(redisHost, redisPort);
			log.info("Connection to server sucessfully"); 
			//check whether server is running or not 
			log.info("Server is running: "+jedis.ping()); 
			response = jedis.set(key, value);
		} catch (Exception e) {
			response = e.getMessage();
			log.error("Error; "+e.getMessage(), e);
		}
		return response;
	}

	@GetMapping(value="/getFromCache/{key}")
	public String getFromCache(@PathVariable(name="key") String key) {
		String response = null;
		log.info("In getFromCache redisHost="+redisHost+", redisPort="+redisPort);
		try {
			Jedis jedis = new Jedis(redisHost, redisPort);
			log.info("Connection to server sucessfully"); 
			//check whether server is running or not 
			log.info("Server is running: "+jedis.ping()); 
			response = jedis.get(key);
		} catch (Exception e) {
			response = e.getMessage();
			log.error("Error; "+e.getMessage(), e);
		}
		return response;
	}

	@GetMapping(value="/testCache")
	public String testCache() {
		String response = null;
		log.info("In testCache redisHost="+redisHost+", redisPort="+redisPort);
		try {
			Jedis jedis = new Jedis(redisHost, redisPort);
			log.info("Connection to server sucessfully"); 
			//check whether server is running or not 
			log.info("Server is running: "+jedis.ping()); 

			//store data in redis list 
			jedis.lpush("tutorial-list", "Redis"); 
			jedis.lpush("tutorial-list", "Mongodb"); 
			jedis.lpush("tutorial-list", "Mysql"); 
			// Get the stored data and print it 
			List<String> list = jedis.lrange("tutorial-list", 0 ,5); 

			StringBuilder sb = new StringBuilder();
			for(int i = 0; i<list.size(); i++) { 
				sb.append("Stored string in redis:: "+list.get(i)); 
			}
			response = sb.toString();
		} catch (Exception e) {
			response = e.getMessage();
			log.error("Error; "+e.getMessage(), e);
		}
		return response;
	}
}
