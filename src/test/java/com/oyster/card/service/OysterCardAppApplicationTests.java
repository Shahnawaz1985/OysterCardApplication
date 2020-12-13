package com.oyster.card.service;
 
import org.junit.Test; 
import org.junit.runner.RunWith; 
import org.springframework.boot.test.context.SpringBootTest; 
import org.springframework.test.context.junit4.SpringRunner;

import com.oyster.card.service.client.OysterCardAppApplication;
 
@RunWith(SpringRunner.class)
@SpringBootTest (classes = {OysterCardAppApplication.class}, 
			properties = {"spring.main.allow-bean-definition-overriding=true"})
public class OysterCardAppApplicationTests {
 @Test 
 public void contextLoads() { }
 
 }
 