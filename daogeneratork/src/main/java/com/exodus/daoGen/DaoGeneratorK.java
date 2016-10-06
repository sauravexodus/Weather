package com.exodus.daoGen;

import org.greenrobot.greendao.generator.DaoGenerator;
import org.greenrobot.greendao.generator.Entity;
import org.greenrobot.greendao.generator.Property;
import org.greenrobot.greendao.generator.Schema;
import org.greenrobot.greendao.generator.ToMany;

public class DaoGeneratorK {

    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(1, "com.exodus.weather.store");

        /*//Add Interests Category Entitiy
        Entity interestCategories = schema.addEntity("InterestCategories");
        interestCategories.addIdProperty().primaryKey();
        interestCategories.addStringProperty("name");
        interestCategories.addStringProperty("image_url");
        interestCategories.addStringProperty("color");
        interestCategories.addIntProperty("priority");

        //Add Interests Entity
        Entity interest = schema.addEntity("Interest");
        interest.addIdProperty().primaryKey();
        interest.addStringProperty("title");

        //Add user entity
        Entity user = schema.addEntity("User");
        user.addIdProperty().primaryKey();
        user.addStringProperty("username");
        user.addStringProperty("name");
        user.addStringProperty("address");
        user.addStringProperty("dob");
        user.addIntProperty("age");
        user.addStringProperty("gender");
        user.addStringProperty("email");
        user.addStringProperty("phone");
        user.addStringProperty("profile_picture");
        user.addStringProperty("cover_picture");
        user.addBooleanProperty("follow_status"); //True if following
        user.addBooleanProperty("follow_request_status"); //True if requested
        user.addIntProperty("following_count");
        user.addIntProperty("followers_count");
        user.addBooleanProperty("do_status");
        user.addBooleanProperty("do_request_status");
        user.addBooleanProperty("dob_visible");
        user.addBooleanProperty("location_visible");
        user.addBooleanProperty("gender_visible");
        user.addBooleanProperty("verified");
        user.addBooleanProperty("requests_allowed");
        user.addStringProperty("shareable_url");
        user.addStringProperty("work");
        user.addStringProperty("education");
        user.addStringProperty("bio");

        //Add user_category join entity
        Entity user_categories = schema.addEntity("UserCategories");
        user_categories.addIdProperty().primaryKey();

        Property user_property = user_categories.addLongProperty("user_id").getProperty();
        Property category_property = user_categories.addLongProperty("category_id").getProperty();
        ToManyWithJoinEntity user_interest_categories_to_user = user.addToMany(interestCategories,
                user_categories, user_property, category_property);
        user_interest_categories_to_user.setName("only_categories");

        //Add user_interests join entity
        Entity user_interests = schema.addEntity("UserInterests");
        user_interests.addIdProperty().primaryKey();

        Property user_property2 = user_interests.addLongProperty("user_id").getProperty();
        Property interest_property = user_interests.addLongProperty("interest_id").getProperty();
        ToManyWithJoinEntity user_interests_to_user = user.addToMany(interest, user_interests,
                user_property2, interest_property);
        user_interests_to_user.setName("interests");

        Property category_property2 = user_interests.addLongProperty("category_id").getProperty();
        ToManyWithJoinEntity user_interest_category_to_user = user.addToMany(interestCategories,
                user_interests, user_property2, category_property2);
        user_interest_category_to_user.setName("categories");

        //Add Feed Element entitiy
        Entity feedElement = schema.addEntity("FeedElement");
        feedElement.addIdProperty();
        feedElement.addIntProperty("type");
        feedElement.addBooleanProperty("home_feeds");
        feedElement.addBooleanProperty("discover_feeds");
        feedElement.addBooleanProperty("search_feeds");
        feedElement.addBooleanProperty("user_feeds");
        feedElement.addLongProperty("belongs_to_user_feeds");
        feedElement.addStringProperty("search_tag");
        feedElement.addBooleanProperty("nearby_feeds");

        //Add post entity
        Entity post = schema.addEntity("Post");
        post.addIdProperty().primaryKey();
        post.addStringProperty("title");
        post.addStringProperty("type");
        post.addStringProperty("description");
        post.addStringProperty("activity_on");
        post.addStringProperty("formatted_address");
        post.addStringProperty("with_text");
        post.addIntProperty("likes_count");
        post.addBooleanProperty("liked");
        post.addIntProperty("reposts_count");
        post.addBooleanProperty("reposted");
        post.addStringProperty("repost_txt");
        post.addIntProperty("comments_count");
        post.addIntProperty("doers_count");
        post.addBooleanProperty("do_status");
        post.addBooleanProperty("do_request_status");
        post.addIntProperty("accepted_requests");
        post.addIntProperty("pending_requests");
        post.addIntProperty("join_requests_count");
        post.addBooleanProperty("invited_to_join");
        post.addBooleanProperty("trending");
        post.addBooleanProperty("recommended");
        post.addDateProperty("updated_at");
        post.addDateProperty("created_at");
        post.addDoubleProperty("trending_quotient");
        post.addStringProperty("shareable_link");
        post.addStringProperty("post_privacy");
        post.addStringProperty("post_visibility");
        post.addStringProperty("distance");
        post.addStringProperty("nearby_type");
        post.addStringProperty("distance_string");

        Property user_feed_property = feedElement.addLongProperty("user_id").getProperty();
        ToMany feed_to_user = user.addToMany(feedElement, user_feed_property);
        feedElement.addToOne(user, user_feed_property);
        feed_to_user.setName("feeds");

        //Add Who to Follow entity
        Entity who_to_follow = schema.addEntity("WhoToFollow");
        who_to_follow.addIdProperty();
        who_to_follow.addLongProperty("feed_element_id");

        Property who_to_follow_property = user.addLongProperty("wtf_id").getProperty();
        ToMany users_to_wtf = who_to_follow.addToMany(user, who_to_follow_property);
        user.addToOne(who_to_follow, who_to_follow_property);
        users_to_wtf.setName("users");

        //Add Suggested card entity
        Entity suggested_card = schema.addEntity("SuggestedCard");
        suggested_card.addIdProperty();
        suggested_card.addStringProperty("title");
        suggested_card.addStringProperty("description");

        //Add Suggested Section entity
        Entity suggested_section = schema.addEntity("SuggestedSection");
        suggested_section.addIdProperty();
        suggested_section.addStringProperty("title");
        suggested_section.addStringProperty("url");
        suggested_section.addStringProperty("shareable_url");

        Property suggested_property = suggested_card.addLongProperty("suggested_section_id").getProperty();
        ToMany suggested_card_to_suggested_section = suggested_section.addToMany(suggested_card, suggested_property);
        suggested_card.addToOne(suggested_section, suggested_property);
        suggested_card_to_suggested_section.setName("suggested_cards");

        //Feed associations
        Property feed_element_property = feedElement.addLongProperty("post_id").getProperty();
        ToMany feed_element_to_post = post.addToMany(feedElement, feed_element_property);
        feedElement.addToOne(post, feed_element_property);
        feed_element_to_post.setName("post");

        Property feed_element_property2 = feedElement.addLongProperty("wtf_id").getProperty();
        ToMany feed_element_to_wtf = who_to_follow.addToMany(feedElement, feed_element_property2);
        feedElement.addToOne(who_to_follow, feed_element_property2);
        feed_element_to_wtf.setName("who_to_follow");

        Property feed_element_property3 = feedElement.addLongProperty("suggested_section_id").getProperty();
        ToMany feed_element_to_suggested_section = suggested_section.addToMany(feedElement, feed_element_property3);
        feedElement.addToOne(suggested_section, feed_element_property3);
        feed_element_to_suggested_section.setName("suggested_section");

        //Add Images entity
        Entity images = schema.addEntity("Image");
        images.addIdProperty().primaryKey();
        images.addStringProperty("url");
        images.addStringProperty("url_1080");
        images.addStringProperty("url_360");
        images.addStringProperty("url_160");

        //Add image associations
        Property image_property = images.addLongProperty("post_id").getProperty();
        ToMany images_to_posts = post.addToMany(images, image_property);
        images.addToOne(post, image_property);
        images_to_posts.setName("images");

        Property suggested_section_property = suggested_section.addLongProperty("image_id").getProperty();
        ToOne image_to_suggested_section = suggested_section.addToOne(images, suggested_section_property);
        image_to_suggested_section.setName("suggested_section_from_image");

        Property suggested_card_property = images.addLongProperty("suggested_card_id").getProperty();
        ToMany image_to_suggested_card = suggested_card.addToMany(images, suggested_card_property);
        images.addToOne(suggested_card, suggested_card_property);
        image_to_suggested_card.setName("images");

        //Add Notifications entity
        Entity notifications = schema.addEntity("Notification");
        notifications.addIdProperty();
        notifications.addStringProperty("notif_type");
        notifications.addBooleanProperty("read");
        notifications.addStringProperty("message");
        notifications.addDateProperty("updated_at");

        Property notifications_to_post = notifications.addLongProperty("post_id").getProperty();
        ToMany notification_to_post = post.addToMany(notifications, notifications_to_post);
        notifications.addToOne(post, notifications_to_post);
        notification_to_post.setName("notifications");

        Property notification_to_image_property = notifications.addLongProperty("image_id").getProperty();
        ToOne notification_to_image = notifications.addToOne(images, notification_to_image_property);
        images.addToMany(notifications, notification_to_image_property);
        notification_to_image.setName("notification_images");

        //Add chat entity
        Entity chat = schema.addEntity("Chat");
        chat.addIdProperty();
        chat.addStringProperty("title");
        chat.addBooleanProperty("seen");
        chat.addStringProperty("last_message");
        chat.addStringProperty("picture");
        chat.addStringProperty("type");
        chat.addLongProperty("user_id");
        chat.addDateProperty("updated_at");
        chat.addStringProperty("status");
        chat.addLongProperty("chat_id");
        chat.addIntProperty("color_id");

        //Add Message entity
        Entity message = schema.addEntity("Message");
        message.addIdProperty();
        message.addStringProperty("text");
        message.addBooleanProperty("seen");
        message.addStringProperty("username");
        message.addDateProperty("created_at");
        message.addBooleanProperty("sent");
        message.addLongProperty("message_id");
        message.addLongProperty("transfer_id");

        //Add Associations
        Property chatId = message.addLongProperty("chat_id").getProperty();
        ToMany chat_to_messages = chat.addToMany(message, chatId);
        message.addToOne(chat, chatId);
        chat_to_messages.setName("messages");

        Property userId = message.addLongProperty("user_id").getProperty();
        ToOne message_to_user = message.addToOne(user, userId);
        message_to_user.setName("user");*/

        Entity city = schema.addEntity("City");
        city.addIdProperty();
        city.addStringProperty("name");
        city.addIntProperty("temperature");
        city.addIntProperty("temp_min");
        city.addIntProperty("temp_max");
        city.addDateProperty("updated_at");
        city.addIntProperty("wind_speed");
        city.addIntProperty("humidity");
        city.addStringProperty("weather_text");
        city.addStringProperty("weather_icon");

        Entity futureWeather = schema.addEntity("FutureWeather");
        futureWeather.addIdProperty();
        futureWeather.addStringProperty("weather_icon");
        futureWeather.addDateProperty("date");
        futureWeather.addIntProperty("temp");

        Property cityId = futureWeather.addLongProperty("city_id").getProperty();
        ToMany city_to_futureWeather = city.addToMany(futureWeather, cityId);
        futureWeather.addToOne(city, cityId);
        city_to_futureWeather.setName("FutureWeather");

        //Generate all classes
        DaoGenerator dg = new DaoGenerator();
        dg.generateAll(schema, "./app/src/main/java");
    }
}
