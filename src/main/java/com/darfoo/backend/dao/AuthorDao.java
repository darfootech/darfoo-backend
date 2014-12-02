package com.darfoo.backend.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.darfoo.backend.model.Author;

@Component
public class AuthorDao {
	@Autowired
	SessionFactory sf;

	/**
	 * 获取所有author对象
	 * @return List<Author>
	 * **/
	public List<Author> getAllAuthor(){
		List<Author> l_author = new ArrayList<Author>();
		try{
			Session session = sf.getCurrentSession();
			String sql = "select * from author";
			l_author = session.createSQLQuery(sql).addEntity(Author.class).list();
		}catch(Exception e){
			e.printStackTrace();
		}
		return l_author;
		
	}
	
	/**
	 * 通过id得到Author对象
	 * @param id 传入要判断的作者的id
	 * @return 表中已经存在该name对应的作者信息,返回Author对象;反之，返回一个null
	 * **/
	public Author getAuthor(Integer id){
		Author author = null;
		try{
			Session session = sf.getCurrentSession();
			String sql = "select * from author where id=:id";
			author = (Author)session.createSQLQuery(sql).addEntity(Author.class).setInteger("id", id).uniqueResult();
		}catch(Exception e){
			e.printStackTrace();
		}
		return author;
	}
	
	/**
	 * 通过name得到Author对象
	 * @param name 传入要判断的作者的name
	 * @return 表中已经存在该name对应的作者信息,返回Author对象;反之，返回一个null
	 * **/
	public Author getAuthor(String name){
		Author author = null;
		try{
			Session session = sf.getCurrentSession();
			String sql = "select * from author where name=:name";
			author = (Author)session.createSQLQuery(sql).addEntity(Author.class).setString("name", name).uniqueResult();
		}catch(Exception e){
			e.printStackTrace();
		}
		return author;
	}
	/**
	 * 根据name判断该author是否已经存在表里
	 * @param name 待判断的author的name
	 * @return 表中已经存在该name对应的作者信息,返回true;反之，返回一个false
	 * **/
	public boolean isExistAuthor(String name){
		boolean isExist = true;
		try{
			Session session = sf.getCurrentSession();
			String sql = "select * from author where name=:name";
			Author author = (Author)session.createSQLQuery(sql).addEntity(Author.class).setString("name", name).uniqueResult();
			isExist = (author==null)?false:true;
		}catch(Exception e){
			e.printStackTrace();
		}
		return isExist;
	}
	/**
	 * 插入单个Author
	 * @param author
	 * **/
	public int insertAuthor(Author author){
		int res = 0;
		try{
			boolean isExist = isExistAuthor(author.getName());
			if(isExist){
				res = CRUDEvent.INSERT_REPEAT;
			}else{
				Session session = sf.getCurrentSession();
				Integer id = (Integer)(session.save(author));
				res = (id>0)?CRUDEvent.INSERT_SUCCESS:CRUDEvent.INSERT_FAIL;
			}
		}catch(Exception e){
			res = CRUDEvent.CRUD_EXCETION;
			//throw new RuntimeException("rollback");			
		}
		return res;
	}
	/**
	 * 单独更新Author。更新的前提是,author已经有对应的name在表中
	 * @param newAuthor 新的author对象
	 * @param oldName 要被更新的auhtor对象的name
	 * **/
	public int updateAuthor(Author newAuthor,String oldName){
		int res = 0;
		try{
			Session session = sf.getCurrentSession();
			Author author = (Author)session.createSQLQuery("select * from author where name=:name")
					.addEntity(Author.class)
					.setString("name", oldName)
					.uniqueResult();
			if(author != null){
				//author即 要被更新的旧的对象
				author.setName(newAuthor.getName());
				author.setDescription(newAuthor.getDescription());
				session.saveOrUpdate(author);
				res = CRUDEvent.UPDATE_SUCCESS;
			}else{
				System.out.println("为空");
				res = CRUDEvent.UPDATE_FAIL;
			}
		}catch(Exception e){
			res = CRUDEvent.CRUD_EXCETION;
		}
		return res;
	}
}
