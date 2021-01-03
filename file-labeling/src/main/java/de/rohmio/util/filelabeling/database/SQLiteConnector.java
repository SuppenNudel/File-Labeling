package de.rohmio.util.filelabeling.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.function.Function;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;

public class SQLiteConnector {

	private String url;

	public SQLiteConnector(String databaseName) {
		initSQLiteDriver(databaseName);
		createBoxesTable();
	}

	private void initSQLiteDriver(String databaseName) {
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		url = String.format("jdbc:sqlite:"+databaseName);
		new ArrayList<>();
	}

	private String table_files = "files";

	private String field_file = "file";
	private String field_labels = "labels";

	// as callback so the actual method does not have to deal with closing the connection
	private <T> T doOperation(Function<DSLContext, T> function) {
		T result = null;
		try (Connection connection = DriverManager.getConnection(url)) {
			DSLContext context = DSL.using(connection, SQLDialect.SQLITE);
			result = function.apply(context);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	private void createBoxesTable() {
		doOperation(context -> context.createTableIfNotExists(DSL.table(table_files))
				.column(DSL.field(field_file, SQLDataType.VARCHAR.nullable(false)))
				.column(DSL.field(field_labels, SQLDataType.VARCHAR))
				.constraint(DSL.primaryKey(DSL.field(field_file)))
				.execute());
	}

}
