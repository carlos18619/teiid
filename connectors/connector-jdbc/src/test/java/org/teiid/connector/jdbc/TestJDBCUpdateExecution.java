/*
 * JBoss, Home of Professional Open Source.
 * See the COPYRIGHT.txt file distributed with this work for information
 * regarding copyright ownership.  Some portions may be licensed
 * to Red Hat, Inc. under one or more contributor license agreements.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301 USA.
 */

package org.teiid.connector.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Arrays;
import java.util.Properties;

import org.junit.Test;
import org.mockito.Mockito;
import org.teiid.connector.api.ConnectorLogger;
import org.teiid.connector.api.ExecutionContext;
import org.teiid.connector.jdbc.translator.Translator;
import org.teiid.connector.language.ICommand;
import org.teiid.connector.language.IInsert;
import org.teiid.connector.language.IInsertExpressionValueSource;
import org.teiid.connector.language.ILiteral;

import com.metamatrix.cdk.api.EnvironmentUtility;

public class TestJDBCUpdateExecution {

	@Test public void testBulkUpdate() throws Exception {
		ICommand command = TranslationHelper.helpTranslate(TranslationHelper.BQT_VDB, "insert into bqt1.smalla (intkey, intnum) values (1, 2)"); //$NON-NLS-1$
		ILiteral value = ((ILiteral)((IInsertExpressionValueSource)((IInsert)command).getValueSource()).getValues().get(0));
		ILiteral value1 = ((ILiteral)((IInsertExpressionValueSource)((IInsert)command).getValueSource()).getValues().get(1));
		value.setMultiValued(true);
		value.setBindValue(true);
		value.setValue(Arrays.asList(1, 2));
		value1.setMultiValued(true);
		value1.setBindValue(true);
		value1.setValue(Arrays.asList(2, 3));
		Connection connection = Mockito.mock(Connection.class);
		PreparedStatement p = Mockito.mock(PreparedStatement.class);
		Mockito.stub(p.executeBatch()).toReturn(new int [] {1, 1});
		Mockito.stub(connection.prepareStatement("INSERT INTO SmallA (IntKey, IntNum) VALUES (?, ?)")).toReturn(p); //$NON-NLS-1$
		Translator sqlTranslator = new Translator();
		ExecutionContext context = EnvironmentUtility.createSecurityContext("user"); //$NON-NLS-1$
		JDBCUpdateExecution updateExecution = new JDBCUpdateExecution(command, connection, sqlTranslator, Mockito.mock(ConnectorLogger.class), new Properties(), context);
		updateExecution.execute();
		Mockito.verify(p, Mockito.times(2)).addBatch();
	}
	
}
