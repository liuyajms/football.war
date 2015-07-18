package cn.com.weixunyun.child;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.ws.rs.core.Application;


import cn.com.weixunyun.child.control.*;
import org.codehaus.jackson.jaxrs.JacksonJaxbJsonProvider;
import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.introspect.JacksonAnnotationIntrospector;
import org.codehaus.jackson.xc.JaxbAnnotationIntrospector;

import cn.com.weixunyun.child.module.broadcast.BroadcastCommentResource;
import cn.com.weixunyun.child.module.broadcast.BroadcastResource;
import cn.com.weixunyun.child.module.cook.CookResource;
import cn.com.weixunyun.child.module.course.classroom.CourseClassroomResource;
import cn.com.weixunyun.child.module.course.evaluation.CourseEvaluationResource;
import cn.com.weixunyun.child.module.course.knowledge.CourseKnowledgeResource;
import cn.com.weixunyun.child.module.course.score.CourseScoreResource;
import cn.com.weixunyun.child.module.curriculum.CurriculumResource;
import cn.com.weixunyun.child.module.download.DownloadResource;
import cn.com.weixunyun.child.module.elective.ElectiveResource;
import cn.com.weixunyun.child.module.elective.ElectiveStudentResource;
import cn.com.weixunyun.child.module.homework.HomeworkResource;
import cn.com.weixunyun.child.module.homework.check.HomeworkCheckResource;
import cn.com.weixunyun.child.module.homework.evaluation.HomeworkEvaluationResource;
import cn.com.weixunyun.child.module.marquee.MarqueeResource;
import cn.com.weixunyun.child.module.marquee.PictureResource;
import cn.com.weixunyun.child.module.news.NewsResource;
import cn.com.weixunyun.child.module.notice.NoticeResource;
import cn.com.weixunyun.child.module.personal.journal.JournalResource;
import cn.com.weixunyun.child.module.personal.photo.PhotoResource;
import cn.com.weixunyun.child.module.point.PointResource;
import cn.com.weixunyun.child.module.question.QuestionResource;
import cn.com.weixunyun.child.module.security.SecurityRecordResource;
import cn.com.weixunyun.child.module.security.SecurityResource;
import cn.com.weixunyun.child.module.star.StarResource;
import cn.com.weixunyun.child.module.stats.StatsResource;
import cn.com.weixunyun.child.module.weibo.WeiboCommentResource;
import cn.com.weixunyun.child.module.weibo.WeiboFavoritResource;
import cn.com.weixunyun.child.module.weibo.WeiboResource;

public class WinkApplication extends Application {

	/**
	 * Get the list of service classes provided by this JAX-RS application
	 */
	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> serviceClasses = new LinkedHashSet<Class<?>>();
		serviceClasses.add(NewsResource.class);
		serviceClasses.add(StarResource.class);
		serviceClasses.add(ContactResource.class);
		serviceClasses.add(TeacherResource.class);
		serviceClasses.add(RoleResource.class);
		// serviceClasses.add(RoleMenuResource.class);
		serviceClasses.add(ClassesResource.class);
		serviceClasses.add(StudentResource.class);
		serviceClasses.add(ParentsResource.class);
		// serviceClasses.add(MenuResource.class);
		serviceClasses.add(DownloadResource.class);
		serviceClasses.add(DictionaryTableResource.class);
		serviceClasses.add(DictionaryFieldResource.class);
		serviceClasses.add(DictionaryValueResource.class);
		serviceClasses.add(RoleResource.class);
		serviceClasses.add(AuthResource.class);
		// serviceClasses.add(PhraseResource.class);
		// serviceClasses.add(PerformanceResource.class);
		serviceClasses.add(GlobalResource.class);
		serviceClasses.add(CookResource.class);
		serviceClasses.add(SchoolResource.class);
		// serviceClasses.add(RegionResource.class);
		serviceClasses.add(BroadcastResource.class);
		serviceClasses.add(CourseResource.class);
		serviceClasses.add(CurriculumResource.class);
		serviceClasses.add(ClassesTeacherResource.class);
		serviceClasses.add(HomeworkResource.class);
		serviceClasses.add(WeiboResource.class);
		serviceClasses.add(WeiboCommentResource.class);
		serviceClasses.add(MessageResource.class);
		serviceClasses.add(BroadcastCommentResource.class);
		serviceClasses.add(WeiboFavoritResource.class);
		serviceClasses.add(ConcernResource.class);
		// serviceClasses.add(StudentSleepResource.class);
		// serviceClasses.add(HomeworkCompleteResource.class);
		// serviceClasses.add(PopedomResource.class);
		serviceClasses.add(FeedbackResource.class);
		serviceClasses.add(HaltResource.class);
		serviceClasses.add(NoticeResource.class);
		serviceClasses.add(ElectiveResource.class);
		serviceClasses.add(ElectiveStudentResource.class);
		serviceClasses.add(SecurityResource.class);
		serviceClasses.add(SecurityRecordResource.class);
		serviceClasses.add(TemplateResource.class);
		serviceClasses.add(CourseKnowledgeResource.class);
		serviceClasses.add(CourseClassroomResource.class);
		serviceClasses.add(CourseEvaluationResource.class);
		
		serviceClasses.add(HomeworkResource.class);
		serviceClasses.add(HomeworkCheckResource.class);
		serviceClasses.add(HomeworkEvaluationResource.class);
		
		serviceClasses.add(StudentGrowResource.class);
		serviceClasses.add(CourseScoreResource.class);
		serviceClasses.add(ChatResource.class);
		serviceClasses.add(PhotoResource.class);
		serviceClasses.add(RuleResource.class);
		serviceClasses.add(MultiResource.class);
		
        serviceClasses.add(PointResource.class);

        serviceClasses.add(MarqueeResource.class);
        serviceClasses.add(JournalResource.class);
        serviceClasses.add(UserMenuResource.class);
        serviceClasses.add(SensitiveResource.class);
        
        serviceClasses.add(StatsResource.class);
        
        serviceClasses.add(QuestionResource.class);
        
        serviceClasses.add(PictureResource.class);

        //获取客户端列表
        ClientResource.initClients();

		return serviceClasses;
	}

	@Override
	public Set<Object> getSingletons() {
		Set<Object> s = new HashSet<Object>();

		// Register the Jackson provider for JSON

		// Make (de)serializer use a subset of JAXB and (afterwards) Jackson
		// annotations
		// See http://wiki.fasterxml.com/JacksonJAXBAnnotations for more
		// information
		try {
			ObjectMapper mapper = new ObjectMapper();
			AnnotationIntrospector primary = new JaxbAnnotationIntrospector();
			AnnotationIntrospector secondary = new JacksonAnnotationIntrospector();
			AnnotationIntrospector pair = new AnnotationIntrospector.Pair(primary, secondary);
			mapper.getDeserializationConfig().setAnnotationIntrospector(pair);
			mapper.getSerializationConfig().setAnnotationIntrospector(pair);

			// Set up the provider
			JacksonJaxbJsonProvider jaxbProvider = new JacksonJaxbJsonProvider();
			jaxbProvider.setMapper(mapper);

			s.add(jaxbProvider);
			return s;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}