package com.examples.rs.currency.web.controller;

import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.examples.rs.business.CurrencyService;

@RestController
public class CurrencyConversionController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	CurrencyService curencyService;

	@RequestMapping(value = "/currency/convert/from/{from}/to/{to}/amount/{amount}", method = {
			RequestMethod.GET }, produces = "application/hal+json")
	public ResponseEntity<BigDecimal> convertCurrency(@PathVariable String from, @PathVariable String to,
			@PathVariable BigDecimal amount, HttpServletRequest request) {

		return new ResponseEntity<BigDecimal>(curencyService.convert(from, to, amount), HttpStatus.OK);
	}

	@RequestMapping(value = "/currency/rate/from/{from}/to/{to}", method = {
			RequestMethod.GET }, produces = "application/hal+json")
	public ResponseEntity<BigDecimal> getCurrencyRate(@PathVariable String from, @PathVariable String to,
			HttpServletRequest request) {

		return new ResponseEntity<BigDecimal>(curencyService.getConversionRate(from, to), HttpStatus.OK);
	}

}