import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.annotation.WebServlet;
import org.apache.commons.logging.LogFactory;
import com.mongodb.Mongo;
import com.mongodb.DBObject;
import com.mongodb.DBCursor;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBList;
import com.mongodb.util.JSON;
import com.mongodb.util.JSONParseException;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.UnknownHostException;
import org.apache.commons.logging.LogFactory;

@WebServlet(name="Syslog",urlPatterns={"/Syslog"})
public class Syslog extends HttpServlet
{
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{

		resp.setContentType("application/json; charset=UTF-8");
		resp.setCharacterEncoding("UTF-8");
		PrintWriter out = resp.getWriter();
		try
		{
			if(req.getParameter("query")!=null)
			{
				DBObject query = (DBObject)JSON.parse(req.getParameter("query"));
				String sort = "DATE";
				boolean asc = false;
				int limit = 20;
				int skip = 0;

				if(req.getParameter("sort")!=null)
				{
					sort = req.getParameter("sort");
				}
				if(req.getParameter("asc")!=null)
				{
					asc = Boolean.parseBoolean(req.getParameter("asc"));
				}
				if(req.getParameter("limit")!=null)
				{
					limit = Integer.parseInt(req.getParameter("limit"));
				}
				if(req.getParameter("skip")!=null)
				{
					skip = Integer.parseInt(req.getParameter("skip"));
				}

				JSONArray result = new JSONArray();
				DBCursor cur = find(query,null,sort,asc,limit,skip);
				for(DBObject card:cur)
				{
					result.put(new JSONObject(card.toString()));
				}
				out.println(result.toString());
			}
			else
			{
				out.println("{\"error\":\"Invalid Query\"}");
			}
		}
		catch(JSONException ex)
		{
			out.println("{\"error\":\"This should never actually happen...\"}");
		}
		catch(JSONParseException ex)
		{
			out.println("{\"error\":\"Invalid Query\"}");
		}
		catch(Exception ex)
		{
			LogFactory.getLog(Syslog.class).error(null,ex);
		}
	}

	public DBCursor find(DBObject query,DBObject fields,String sort,boolean asc,int limit,int skip)
	{
		DBCursor cur = MongoFactory.getMongo().getDB("syslog").getCollection("messages").find(query,fields);

		if(sort!=null && sort.length()>0)
		{
			if(asc)
			{
				cur = cur.sort(new BasicDBObject(sort,1));
			}
			else
			{
				cur = cur.sort(new BasicDBObject(sort,-1));
			}
		}

		if(limit>0)
		{
			cur = cur.limit(limit);
		}

		if(skip>0)
		{
			cur = cur.skip(skip);
		}

		return cur;
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{
		doGet(req, resp);
	}
}
