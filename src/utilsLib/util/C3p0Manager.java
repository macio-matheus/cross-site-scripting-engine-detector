package utilsLib.util;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;


import com.mchange.v2.c3p0.*;

import system.*;
import utilsLib.jsp.*;


public class C3p0Manager extends ConnManager {
	public static  String DEFAULT_DRIVER = "org.gjt.mm.mysql.Driver";  
	
	private PooledDataSource pooled;
	
	public static String SYNCH_C3P0 = "";
	private int gots; 

	public C3p0Manager(DbConsultManager consultManager)
			throws ClassNotFoundException, SQLException, IOException,
			FileNotFoundException {
		super(consultManager);
	refresh();
	}

	public void refresh() {
		if (pooled == null) {
			try {
				Class.forName(DEFAULT_DRIVER).newInstance();

				// Note: your JDBC driver must be loaded [via Class.forName( ...
				// ) or -Djdbc.properties]
				// prior to acquiring your DataSource!

				// Acquire the DataSource... this is the only c3p0 specific code
				// here
//				DataSource unpooled = DataSources
//				.unpooledDataSource("jdbc:mysql://localhost/base?autoReconnect=true&amp;useUnicode=true&amp;jdbcCompliantTruncation=false",
//						"root", "...");
//				unpooled.
				DataSource unpooled = DataSources.unpooledDataSource();
				pooled = (PooledDataSource)DataSources.pooledDataSource(unpooled);
			} catch (Exception e) {
				JSPUtils.logError("[C3p0Managr.init()] Erro: ", e);
			}
		}
	}

	public int getInUseConnectionCount(int connIndex) {
		return 0;
	}

	public int getAvailableConnectionCount(int connIndex) {
		return 0;
	}

	public void refreshConn(int connIndex) throws SQLException,
			ClassNotFoundException, IOException, FileNotFoundException {
	}

	public void release(Connection conn) throws SQLException {
		try {
			if (conn != null)
				conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int getMax(int connIndex) {
		return 0;
	}
	
	public void printStats() throws SQLException {
		System.out.println("-------------");
		System.out.println("conexões ocupadas: " + pooled.getNumBusyConnections());
		System.out.println("total de conexões: " + pooled.getNumConnections());
		System.out.println("-------------");
	}

	public Connection getConn(int connIndex) {
		try {
//			printStats();
			
			synchronized (SYNCH_C3P0) {
				gots++;
			}
				

			return pooled.getConnection();
		} catch (Exception e) {
			JSPUtils.logError("[C3p0Managr.getConn()] Erro: ", e);

			return null;
		}
	}
	
	public PooledDataSource getPooled() {
		return pooled;
	}

	public void setPooled(PooledDataSource pooled) {
		this.pooled = pooled;
	}
	
	public int getGots() {
		return gots;
	}

	public void setGots(int gots) {
		this.gots = gots;
	}
}
