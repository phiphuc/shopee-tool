package com.hunre.phinp.config.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConfiguration {

    @Value("${spring.spring.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${cloud.group}")
    private String group;

    @Value("${cloud.timeout}")
    private String timeout;

    @Value("${cloud.topic.login.request}")
    private String login;

    @Value("${cloud.topic.otp.request}")
    private String otp;

    @Value("${cloud.topic.follow.request}")
    private String follow;

    @Value("${cloud.topic.unfollow.request}")
    private String unfollow;

    @Value("${cloud.topic.like.request}")
    private String like;

    @Value("${cloud.topic.unlike.request}")
    private String unlike;

    @Value("${cloud.topic.username.request}")
    private String username;

    @Value("${cloud.topic.information-shop.request}")
    private String information;

    @Value("${cloud.topic.items-shop.request}")
    private String items;

    @Value("${cloud.topic.view.request}")
    private String view;


    @Bean
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, group);

        return props;
    }

    @Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        return props;
    }

    @Bean
    public CompletableFutureReplyingKafkaOperations<String, String, String> replyKafkaTemplate() {
        CompletableFutureReplyingKafkaTemplate<String, String, String> requestReplyKafkaTemplate =
            new CompletableFutureReplyingKafkaTemplate<>(requestProducerFactory(),
                replyListenerContainer());
        requestReplyKafkaTemplate.setDefaultTopic("default");
        requestReplyKafkaTemplate.setReplyTimeout(Long.parseLong(timeout));
        return requestReplyKafkaTemplate;
    }

    @Bean
    public ProducerFactory<String, String> requestProducerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    public ConsumerFactory<String, String> replyConsumerFactory() {
        JsonDeserializer<String> jsonDeserializer = new JsonDeserializer<>();
        jsonDeserializer.addTrustedPackages(String.class.getPackage().getName());
        return new DefaultKafkaConsumerFactory<>(consumerConfigs(), new StringDeserializer(),
            jsonDeserializer);
    }

    @Bean
    public KafkaMessageListenerContainer<String, String> replyListenerContainer() {
        ContainerProperties containerProperties = new ContainerProperties("default");
        return new KafkaMessageListenerContainer<>(replyConsumerFactory(), containerProperties);
    }

    @Bean
    public KafkaAdmin admin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic replyLogin() {
        Map<String, String> configs = new HashMap<>();
        configs.put("retention.ms", timeout);
        return new NewTopic(login, 2, (short) 2).configs(configs);
    }

    @Bean
    public NewTopic replyOtp() {
        Map<String, String> configs = new HashMap<>();
        configs.put("retention.ms", timeout);
        return new NewTopic(otp, 2, (short) 2).configs(configs);
    }

    @Bean
    public NewTopic replyLike() {
        Map<String, String> configs = new HashMap<>();
        configs.put("retention.ms", timeout);
        return new NewTopic(like, 2, (short) 2).configs(configs);
    }

    @Bean
    public NewTopic replyUnlike() {
        Map<String, String> configs = new HashMap<>();
        configs.put("retention.ms", timeout);
        return new NewTopic(unlike, 2, (short) 2).configs(configs);
    }

    @Bean
    public NewTopic replyFollow() {
        Map<String, String> configs = new HashMap<>();
        configs.put("retention.ms", timeout);
        return new NewTopic(follow, 2, (short) 2).configs(configs);
    }

    @Bean
    public NewTopic replyUnfollow() {
        Map<String, String> configs = new HashMap<>();
        configs.put("retention.ms", timeout);
        return new NewTopic(unfollow, 2, (short) 2).configs(configs);
    }

    @Bean
    public NewTopic replyUsername() {
        Map<String, String> configs = new HashMap<>();
        configs.put("retention.ms", timeout);
        return new NewTopic(username, 2, (short) 2).configs(configs);
    }

    @Bean
    public NewTopic replyInformation() {
        Map<String, String> configs = new HashMap<>();
        configs.put("retention.ms", timeout);
        return new NewTopic(information, 2, (short) 2).configs(configs);
    }

    @Bean
    public NewTopic replyItem() {
        Map<String, String> configs = new HashMap<>();
        configs.put("retention.ms", timeout);
        return new NewTopic(items, 2, (short) 2).configs(configs);
    }

    @Bean
    public NewTopic replyView() {
        Map<String, String> configs = new HashMap<>();
        configs.put("retention.ms", timeout);
        return new NewTopic(view, 2, (short) 2).configs(configs);
    }
}
