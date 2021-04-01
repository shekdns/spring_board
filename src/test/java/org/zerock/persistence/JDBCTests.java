package org.zerock.persistence;

import static org.junit.Assert.fail;

import java.sql.Connection;
import java.sql.DriverManager;

import org.junit.Test;

import lombok.extern.log4j.Log4j;

/*
 *jdbc接続テストコード
 */
@Log4j
public class JDBCTests {
	
	static {
		try {
			Class.forName("net.sf.log4jdbc.sql.jdbcapi.DriverSpy");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//oracle.jdbc.pool.OracleDataSource
	//oracle.jdbc.OracleDriver
	@Test
	public void testConnection() {

		try (Connection con = DriverManager.getConnection("jdbc:oracle:thin:@shekdns_high?TNS_ADMIN=D:/wallet", "admin",
				"dlwnghks123L")) {

			log.info(con);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

}
