package com.albiworks.test.spdatamongo.resources;

import java.math.BigInteger;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.albiworks.test.spdatamongo.domain.Message;
import com.albiworks.test.spdatamongo.repository.MessageRepository;

/**
 * Simple Rest resource to manage the messages that will be posted 
 * @author alexblanco
 *
 */
@RestController
@RequestMapping(path="/msg", produces=MediaType.APPLICATION_JSON_VALUE)
public class MessageResource {

	@Autowired
	private MessageRepository repo;
	
	@RequestMapping(method=RequestMethod.POST, consumes=MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<Void> postMessage(@RequestBody String message, 
			@RequestHeader(name="Author", required=false) String author,
			UriComponentsBuilder uriBuilder){
		Message m = new Message(message);
		if (author != null)
			m.setAuthor(author);
		repo.save(m);
		BigInteger id = m.getId();
		URI loc = uriBuilder.path("/msg/{id}").buildAndExpand(id).toUri();
		return ResponseEntity.created(loc).build();
	}
	
	
	@RequestMapping(path="/{id}",method=RequestMethod.GET)
	public ResponseEntity<Message> getMessage(@PathVariable(value="id")BigInteger id){
		Message m = repo.findOne(id);
		
		return Optional.ofNullable(m)
				.map(message -> ResponseEntity.ok().body(message))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
	
	
	@RequestMapping(path="/list",method=RequestMethod.GET)
	public ResponseEntity<List<Message>> getMessages(){
		Iterable<Message> messages = repo.findAll();
		List<Message> results = StreamSupport.stream(messages.spliterator(), false).collect(Collectors.toList());
		
		return ResponseEntity.ok(results);
	}
	
}
