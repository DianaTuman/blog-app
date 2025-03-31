package com.dianatuman.practicum.repository;

import com.dianatuman.practicum.configuration.DataSourceConfiguration;
import com.dianatuman.practicum.model.Comment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringJUnitConfig({DataSourceConfiguration.class, JdbcNativeCommentRepository.class})
public class JdbcNativeCommentRepositoryTest {


}
