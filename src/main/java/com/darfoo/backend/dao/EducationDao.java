package com.darfoo.backend.dao;

import java.util.*;

import com.darfoo.backend.model.*;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.darfoo.backend.model.Education;

@Component
public class EducationDao {
	@Autowired
	private SessionFactory sf;
	
	//插入所有education(视频)的类型  (暂时将名师教学这个选项去掉)
	public void insertAllEducationCategories(){
		String[] categories = {"快","中","慢",    //按速度
				"简单","适中","稍难",					//按难度  
				"队形表演","背面教学","分解教学"};  //按教学类型

		try{
			Session session = sf.getCurrentSession();
			for(String category : categories){
				EducationCategory eCategory = new EducationCategory();
				eCategory.setTitle(category);
				eCategory.setDescription("待定");
				session.save(eCategory);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	//插入单个education视频
	@SuppressWarnings("unchecked")
	public int insertSingleEducationVideo(Education video){
		Set<EducationCategory> eCategories = video.getCategories();
		Author author = video.getAuthor();
		Image image = video.getImage();
		try{
			Session session = sf.getCurrentSession();
			//先查询image表中是否包含此video的图片信息
			Criteria c = session.createCriteria(Image.class).add(Restrictions.eq("image_key", image.getImage_key()));//image的image_key字段需为unique
			List<Image> l_img = c.list();
			if(l_img.size() > 0){
				//image表包含该video的图片信息,用持久化的实体代替原来的值
				video.setImage(l_img.get(0)); 
			}
			//查询author表中是否包含此video的作者信息
			c = session.createCriteria(Author.class).add(Restrictions.eq("name", author.getName())); //author的name字段需为unique
			List<Author> l_author = c.list();
			if(l_author.size() > 0){
				//author表包含该video的作者信息,用持久化的实体代替原来的值
				video.setAuthor(l_author.get(0));
			}
			//对于VideoCategory，插入时默认认为全都属于VideoCategory表，所以只需找到对应种类的实体即可
			Set<String> s_title = new HashSet<String>();  //videoCategory的title字段为unique
			for(EducationCategory eCategory : eCategories){
				s_title.add(eCategory.getTitle());
			}
			c = session.createCriteria(EducationCategory.class).add(Restrictions.in("title", s_title));
			List<EducationCategory> l_vCategory = c.list();   
			eCategories = new HashSet<EducationCategory>(l_vCategory);
			video.setCategories(eCategories);
			session.saveOrUpdate(video);
            int insertId = video.getId();
            return insertId;
		}catch(Exception e){
			e.printStackTrace();
            return -1;
		}
	}
	/**
	 * 获取单个enducation video的信息
	 * 根据video的id来获得video对象
	 * @return video 返回一个video的实例对象(包含关联表中的数据),详细看Education类
	 * **/
	public Education getEducationVideoById(Integer vid){
		Education video = null;
		try{
			Session session = sf.getCurrentSession();
			Criteria c = session.createCriteria(Education.class);
			c.setReadOnly(true);
			c.add(Restrictions.eq("id", vid));
			//设置JOIN mode，这样categories会直接加载到video类中
			c.setFetchMode("categories", FetchMode.JOIN);  
			video = (Education)c.uniqueResult();
		}catch(Exception e){
			e.printStackTrace();
		}
		return video;
	}

    /**
     * 获取单个enducation video的信息
     * 根据video的title来获得video对象
     * @return video 返回一个video的实例对象(包含关联表中的数据),详细看Education类
     * **/
    public Education getEducationVideoByTitle(String title){
        Education video = null;
        try{
            Session session = sf.getCurrentSession();
            Criteria c = session.createCriteria(Education.class);
            c.setReadOnly(true);
            c.add(Restrictions.eq("title", title));
            //设置JOIN mode，这样categories会直接加载到video类中
            c.setFetchMode("categories", FetchMode.JOIN);
            video = (Education)c.uniqueResult();
        }catch(Exception e){
            e.printStackTrace();
        }
        return video;
    }

    /**
     * 获取单个tutorial的信息
     * 根据tutorial的title和作者id来获得tutorial对象
     * @return education 返回一个Education的实例对象(包含关联表中的数据)，详细请看Education.java类
     * **/
    public Education getEducationByTitleAuthorId(String title, int authorid){
         Education education = null;
        try{
            Session session = sf.getCurrentSession();
            String sql = "select * from education where title=:title and author_id=:authorid";
            education = (Education)session.createSQLQuery(sql).addEntity(Education.class).setString("title", title).setInteger("authorid", authorid).uniqueResult();
        }catch(Exception e){
            e.printStackTrace();
        }
        return education;
    }

	/**
	 * 根据类别获取视频列表(我要上网—视频页面) 暂时去掉名师那个种类
	 * $categories  (快-简单—队形表演) 如果用户没有选择某个类别，那么就去掉该字符串
	 * @param 例如 categories = {"快","简单","队形表演"}  例如 categories = {"快","简单"} 
	 * **/
	public List<Education> getEducationVideosByCategories(String[] categories){
		List<Education> l_video = new ArrayList<Education>();
		try{
			Session session = sf.getCurrentSession();
			List<Integer> l_interact_id = new ArrayList<Integer>();  //存符合部分条件的video id
			Criteria c;
			for(int i=0;i<categories.length;i++){
				c = session.createCriteria(Education.class).setProjection(Projections.property("id"));
				c.createCriteria("categories").add(Restrictions.eq("title", categories[i]));
				c.setReadOnly(true);
				List<Integer> l_id = c.list();	
				System.out.println("满足条件 "+categories[i]+" 的enducation video数量》》》"+l_id.size() );
				for(Integer j : l_id){
					System.out.print(j+"#");					
				}
				System.out.println();
				if(l_id.size() == 0){
					//只要有一项查询结果长度为0，说明视频表无法满足该种类组合，返回一个空的List<Education>对象,长度为0
					l_video = new ArrayList<Education>();
					l_interact_id.clear();//清空
					break;
				}else{
					if(l_interact_id.size() == 0){
						l_interact_id = l_id;
						continue;
					}else{
						l_interact_id.retainAll(l_id);
						boolean hasItersection = l_interact_id.size()>0?true:false;
						if(!hasItersection){
							//之前查询的结果与当前的无交集，说明视频表无法满足该种类组合，返回一个空的List<Education>对象,长度为0
							l_video = new ArrayList<Education>();
							break;
						}
					}
				}
			}	
			if(categories.length==0){
				//categories长度为0，即没有筛选条件,返回所有视频
				c = session.createCriteria(Education.class);
				c.setReadOnly(true);
				l_video = c.list();
			}else if(l_interact_id.size() > 0){
				//交集内的id数量大于0个
				c = session.createCriteria(Education.class).add(Restrictions.in("id", l_interact_id));
				c.setReadOnly(true);
				l_video = c.list();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return l_video;
	}
	
	/**
	 * 根据id删除单个教学视频
	 * educationcategory表不受影响
	 * **/
//	public int deleteEducationById(Integer id){
//		int result = 0;
//		try{
//			Session session = sf.getCurrentSession();
//			String sql1 = "delete from education_category where video_id=:video_id";
//			String sql2 = "delete from education where id=:id";
//			int res = session.createSQLQuery(sql1).setInteger("video_id", id).executeUpdate();  
//			if(res > 0){
//				result = session.createSQLQuery(sql2).setInteger("id", id).executeUpdate();
//			}
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//		return result;
//	}
	
	/**
	 * 删除(education和关系表中的值) 
	 * **/
	public int deleteEducationById(Integer id){
		int res = 0;
		try{
			Session session = sf.getCurrentSession();
			Education education = (Education)session.get(Education.class, id);
			if(education == null){
				res = CRUDEvent.DELETE_NOTFOUND;
			}else{
                education.setAuthor(null);
                session.update(education);
				session.delete(education);
				res = CRUDEvent.DELETE_SUCCESS;
			}			
		}catch(Exception e){
			e.printStackTrace();
			res = CRUDEvent.DELETE_FAIL;
		}
		return res;
	}
	/**
	 * 更新Education之前先做check(主要是对author和image的check)
	 * ***不对title(key)进行update，这样的更新没有必要，不如直接插入一个Video(吉卉这个请注意)
	 * @param id 需要更新的对象对应的id
	 * @param authorname 新的作者名字  (null值表示不需要更新)
	 * @param imagekey   新的图片key (null值表示不需要更新)
	 * **/
	public  UpdateCheckResponse updateEducationCheck(Integer id, String authorname, String imagekey){
		UpdateCheckResponse response = new UpdateCheckResponse();
		Education oldEducation = null;
		try{
			Session session = sf.getCurrentSession();
			oldEducation = (Education)session.get(Education.class, id);
			if(oldEducation == null){
				System.out.println("要更新的Education不存在");
				response.setEducationUpdate(1);
			}else{
				if(authorname != null){
					if(!authorname.equals(oldEducation.getAuthor().getName())){
						Criteria c = session.createCriteria(Author.class).add(Restrictions.eq("name", authorname));
						c.setReadOnly(true);
						Author a = (Author)c.uniqueResult();
						if(a == null){
							System.out.println("要更新的education的作者不存在，请先完成作者信息的插入");
							response.setAuthorUpdate(1);  
						}
					}
				}
				if(imagekey != null){
					if(!imagekey.equals(oldEducation.getImage().getImage_key())){
						Criteria c = session.createCriteria(Image.class).add(Restrictions.eq("image_key", imagekey));
						c.setReadOnly(true);
						Image a = (Image)c.uniqueResult();
						if(a == null){
							System.out.println("要更新的education的插图不存在，请完成图片插入");
							response.setImageUpdate(1);
						}
					}
				}
//				if(title !=null){
//					if(!title.equals(oldVideo.getTitle())){
//						System.out.println("修改title就意味着修改key，也就表示需要修改云端存着的资源的key,还不如直接插入一个新的");
//						response.setTitleUpdate(1);
//					}
//				}			
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return response;
	}
	
	/**
	 * update Edcation
	 * 在update之前，请务必做updateEducationCheck操作，保证UpdateCheckResponse.updateIsReady()==true;若为false,请根据response的成员值来设计逻辑
	 * 注意：一定要保证UpdateCheckResponse.updateIsReady()==true后再进行update操作
	 * @param id 需要更新的对象对应的id
	 * @param authorname 新的作者名字(null值表示不需要更新)
	 * @param imagekey   新的图片key(null值表示不需要更新)
	 * @param categoryTitles  种类的集合(null值表示不需要更新)
	 * **/
	public int updateEducation(Integer id,String authorname, String imagekey, Set<String> categoryTitles,Long updateTimestamp){
		int res = 0;
		try{
			Session session = sf.getCurrentSession();
			Education oldEducation = (Education)session.get(Education.class, id);
			//check操作保证作者信息已经在author表中
			if(authorname != null){
				Criteria c = session.createCriteria(Author.class).add(Restrictions.eq("name", authorname));
				Author author = (Author)c.uniqueResult();
				if(author != null){
					oldEducation.setAuthor(author);
				}else{
					return res = CRUDEvent.UPDATE_AUTHOR_NOTFOUND;
				}
			}else{
				System.out.println("作者不需要更新");
			}
			//check操作保证图片信息已经在image表中
			if(imagekey != null){
				Criteria c = session.createCriteria(Image.class).add(Restrictions.eq("image_key", imagekey));
				Image image = (Image)c.uniqueResult();
				if(image != null){
					oldEducation.setImage(image);
				}else{
					return res = CRUDEvent.UPDATE_IMAGE_NOTFOUND;
				}
			}else{
				System.out.println("图片不需要更新");
			}
			//默认认为种类值已经都在数据库中
			Set<EducationCategory> s_vCategories = oldEducation.getCategories();
			if(categoryTitles!=null && categoryTitles.size() > 0){
				//存在需要更新的种类,从表中获得对应的种类对象
				List<EducationCategory> l_Categories = session.createCriteria(EducationCategory.class).add(Restrictions.in("title", categoryTitles)).setReadOnly(true).list();
				if(l_Categories.size() > 0){
					s_vCategories.clear();//删除原始关联
					oldEducation.setCategories(new HashSet<EducationCategory>(l_Categories)); //增加新的关联
				}
			}else{
				System.out.println("种类不需要更新");
			}
			if(updateTimestamp != null)
				oldEducation.setUpdate_timestamp(updateTimestamp);
			System.out.println("-----更新后的education如下-----");
			System.out.println(oldEducation.toString(true));
			session.saveOrUpdate(oldEducation);
			res = CRUDEvent.UPDATE_SUCCESS;
		}catch(Exception e){
			res = CRUDEvent.CRUD_EXCETION;
			e.printStackTrace();
		}
		return res;
	}

    public int updateVideoKeyById(int tutorialid, String newTutorialKey){
        int res = 0;
        try{
            Session session = sf.getCurrentSession();
            Education oldTutorial = (Education)session.get(Education.class, tutorialid);
            oldTutorial.setVideo_key(newTutorialKey);
            session.saveOrUpdate(oldTutorial);
            res = CRUDEvent.UPDATE_SUCCESS;
        }catch (Exception e){
            res = CRUDEvent.CRUD_EXCETION;
            e.printStackTrace();
        }
        return res;
    }
	
	/**
	 * 获得所有Education对象
	 * **/
	public List<Education> getAllEdutcaion(){
		List<Education> s_educations = new ArrayList<Education>();
        Map<Integer, Education> sortMap = new HashMap<Integer, Education>();

        try{
			Session session = sf.getCurrentSession();
			Criteria c = session.createCriteria(Education.class);
			c.setReadOnly(true);
			c.setFetchMode("categories", FetchMode.JOIN);
			List<Education> l_educations = c.list();  
			if(l_educations.size() > 0){
                for (Education education : l_educations){
                    sortMap.put(education.getId(), education);
                }
            }

            List<Integer> sortedKeys=new ArrayList(sortMap.keySet());
            Collections.sort(sortedKeys);

            for (Integer index : sortedKeys){
                s_educations.add(sortMap.get(index));
            }

		}catch(Exception e){
			e.printStackTrace();
		}

        Collections.reverse(s_educations);
        return s_educations;
	}
}
