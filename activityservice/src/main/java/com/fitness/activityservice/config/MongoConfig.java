package com.fitness.activityservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

// this configuration file is to enable MongoDB auditing features, which allows tracking of entity creation and modification timestamps.
@Configuration
@EnableMongoAuditing
public class MongoConfig {

}
