package com.albiworks.test.spdatamongo.repository;

import java.math.BigInteger;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;

import com.albiworks.test.spdatamongo.domain.Message;

@Service
public interface MessageRepository extends PagingAndSortingRepository<Message, BigInteger>{

}
