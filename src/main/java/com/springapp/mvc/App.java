package com.springapp.mvc;

import com.darfoo.backend.model.HibernateUtil;
import com.darfoo.backend.model.*;
import org.hibernate.Session;

/**
 * Created by zjh on 14-11-19.
 */
public class App {
    public static void main(String[] args){
        Session session = HibernateUtil.getSessionFactory().openSession();

        session.beginTransaction();

        Author author = new Author();
        session.save(author);

        Image image = new Image("image_key");
        session.save(image);

        Music music = new Music();
        session.save(music);

        MusicCategory musicCategory = new MusicCategory();
        session.save(musicCategory);

        Video video = new Video();
        session.save(video);

        VideoCategory videoCategory = new VideoCategory();
        session.save(videoCategory);

        session.getTransaction().commit();
        System.out.println("it works");
    }
}
