package com.dianatuman.practicum.repository;

import com.dianatuman.practicum.configuration.DataSourceConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig({DataSourceConfiguration.class, JdbcNativePostRepository.class})
public class JdbcNativePostRepositoryTest {

}
