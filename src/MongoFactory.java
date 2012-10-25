import java.net.UnknownHostException;
import com.mongodb.Mongo;
import javax.servlet.annotation.WebListener;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletContextEvent;
import org.apache.commons.logging.LogFactory;

@WebListener
public class MongoFactory implements ServletContextListener
{
	private static Mongo mongo;

	@Override
	public void contextInitialized(ServletContextEvent sce)
	{
		try
		{
			mongo = new Mongo();
		}
		catch(UnknownHostException ex)
		{
			LogFactory.getLog(MongoFactory.class).error(null,ex);
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce)
	{
		mongo.close();
		mongo = null;
	}

	public static Mongo getMongo()
	{
		return mongo;
	}
}
