package com.darfoo.backend.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.darfoo.backend.model.Author;
import com.darfoo.backend.model.Image;
import com.darfoo.backend.model.UpdateCheckResponse;
import com.darfoo.backend.model.Author;

@Component
@SuppressWarnings("unchecked")
public class AuthorDao {
	@Autowired
	SessionFactory sf;
	
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
	 * 在插入时请保证image有值，否则将以null
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
	 * 获取所有author对象
	 * @return List<Author>
	 * **/
	public List<Author> getAllAuthor(){
		List<Author> l_author = new ArrayList<Author>();
		try{
			Session session = sf.getCurrentSession();
			String sql = "select * from author order by id desc";
			l_author = session.createSQLQuery(sql).addEntity(Author.class).list();
		}catch(Exception e){			
			e.printStackTrace();
		}
		return l_author;
	}
	
	/**
	 * 更新Author之前先做check(主要是对image的check) 
	 * @param id 需要更新的对象对应的id
	 * @param imagekey   新的图片key (null值表示不需要更新)
	 * @return response  里面包含check的结果
	 * **/
	public  UpdateCheckResponse updateAuthorCheck(Integer id, String imagekey){
		UpdateCheckResponse response = new UpdateCheckResponse();
		Author author = null;
		try{
			Session session = sf.getCurrentSession();
			author = (Author)session.get(Author.class, id);
			if(author == null){
				System.out.println("要更新的Author不存在");
				response.setAuthorUpdate(1);
			}else{
				if(imagekey != null && author.getImage() != null){
					if(!imagekey.equals(author.getImage().getImage_key())){
						Criteria c = session.createCriteria(Image.class).add(Restrictions.eq("image_key", imagekey));
						c.setReadOnly(true);
						Image a = (Image)c.uniqueResult();
						if(a == null){
							System.out.println("要更新的Author的插图不存在，请完成图片插入");
							response.setImageUpdate(1);
						}
					}
				}			
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return response;
	}
	/**
	 * 单独更新Author。更新的前提是,author已经有对应的name在表中
	 * @param id         需要被更新的author的id
	 * @param authorName 新的author对象的名字(null表示不更新)
	 * @param authorDescription 要被更新的author对象的描述(null表示不更新)
	 * @param imageKey 作者对应的图片
	 * **/
	public int updateAuthor(Integer id,String authorName,String authorDescription,String imageKey){
		int res = 0;
		try{
			Session session = sf.getCurrentSession();
			Author author = (Author)session.get(Author.class, id);
			if(author != null){
				//check操作保证图片信息已经在image表中
				if(imageKey != null){
					Criteria c = session.createCriteria(Image.class).add(Restrictions.eq("image_key", imageKey));
					Image image = (Image)c.uniqueResult();
					if(image != null){
						author.setImage(image);
					}else{
						return res = CRUDEvent.UPDATE_IMAGE_NOTFOUND;
					}
				}else{
					System.out.println("图片不需要更新");
				}
				if(authorName!=null)
					author.setName(authorName);
				if(authorDescription!=null)
					author.setDescription(authorDescription);
				session.saveOrUpdate(author);
				res = CRUDEvent.UPDATE_SUCCESS;
			}else{
				System.out.println("没有找到此id对应的author对象");
				res = CRUDEvent.UPDATE_AUTHOR_NOTFOUND;
			}
		}catch(Exception e){
			res = CRUDEvent.CRUD_EXCETION;
		}
		return res;
	}
	
	/**
	 * 删除作者
	 * (先解除与video music education对应author_id的约束，现将author_id设为null)
	 * **/
	public int deleteAuthorById(Integer id){
		int res = 0;
		try{
			Session session = sf.getCurrentSession();
			Author author = (Author)session.get(Author.class, id);
			if(author == null){
				System.out.println("没有找到对应id为"+id+"的Author");
				res = CRUDEvent.DELETE_NOTFOUND;
			}else{
				String sql1 = "update video set AUTHOR_ID=null where AUTHOR_ID=:author_id";		
				String sql2 = "update education set AUTHOR_ID=null where AUTHOR_ID=:author_id";	
				String sql3 = "update music set AUTHOR_ID=null where AUTHOR_ID=:author_id";	
				System.out.println("video受影响的行数:"+session.createSQLQuery(sql1).setInteger("author_id", id).executeUpdate());
				System.out.println("education受影响的行数:"+session.createSQLQuery(sql2).setInteger("author_id", id).executeUpdate());
				System.out.println("music受影响的行数:"+session.createSQLQuery(sql3).setInteger("author_id", id).executeUpdate());
				session.delete(author);
				res = CRUDEvent.DELETE_SUCCESS;
			}
		}catch(Exception e){
			res = CRUDEvent.DELETE_FAIL;
			e.printStackTrace();
		}
		return res;
	}
}
