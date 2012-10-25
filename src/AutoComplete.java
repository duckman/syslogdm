import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.annotation.WebServlet;
import org.apache.commons.logging.LogFactory;
import com.mongodb.Mongo;
import com.mongodb.DBObject;
import com.mongodb.DBCollection;
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

@WebServlet(name="AutoComplete",urlPatterns={"/AutoComplete"})
public class AutoComplete extends HttpServlet
{
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{

		resp.setContentType("application/json; charset=UTF-8");
		resp.setCharacterEncoding("UTF-8");
		PrintWriter out = resp.getWriter();

		DBCollection coll = MongoFactory.getMongo().getDB("syslog").getCollection("messages");
		BasicDBObject rtrn = new BasicDBObject();
		rtrn.put("hosts",coll.distinct("HOST"));
		rtrn.put("prios",coll.distinct("PRIORITY"));
		rtrn.put("progs",coll.distinct("PROGRAM"));

		out.print(rtrn.toString());
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{
		doGet(req, resp);
	}
}
