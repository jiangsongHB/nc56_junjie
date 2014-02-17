/**
 * 
 */
package nc.impl.so.taxinvoice;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.NamingException;

import nc.bs.dao.BaseDAO;
import nc.bs.pub.DataManageObject;
import nc.bs.uap.util.JdbcSessionUtil;
import nc.jdbc.framework.JdbcSession;
import nc.jdbc.framework.PersistenceManager;
import nc.jdbc.framework.crossdb.CrossDBConnection;
import nc.jdbc.framework.exception.DbException;
import nc.vo.so.TaxInvoiceBbVO;
import nc.vo.so.TaxInvoiceDealVO;

/**
 * @author ausu
 *
 */
public class TaxInvoiceDMO extends DataManageObject {

	/**
	 * @throws NamingException
	 */
	public TaxInvoiceDMO() throws NamingException {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param dbName
	 * @throws NamingException
	 */
	public TaxInvoiceDMO(String dbName) throws NamingException {
		super(dbName);
		// TODO Auto-generated constructor stub
	}

	public void saveDeal(TaxInvoiceBbVO[] dealvo) throws SQLException{
		
			CrossDBConnection con = null;
			
			PreparedStatement stmt = null;

			String sql = null;
			try {
				con = (CrossDBConnection) this.getConnection(); 
				con.setAutoCommit(false);
				con.setSqlTrans(true);
				for(int i=0; i<dealvo.length; i++){
					sql = "update so_taxinvoice_b  set ntotaldealmny = isnull(ntotaldealmny,0) + ?, ntotaldealnum = isnull(ntotaldealnum,0) + ? where ctaxinvoice_bid = ?";
					stmt = con.prepareStatement(sql);
					stmt.clearParameters();
					stmt.setDouble(1, dealvo[i].getNdealmny().doubleValue());
					stmt.setDouble(2, dealvo[i].getNdealnum().doubleValue());
					stmt.setString(3, dealvo[i].getCtaxinvoice_bid());
					stmt.executeUpdate();
					sql = "update so_saleinvoice_b set ntotaldealmny = isnull(ntotaldealmny,0) + ?, ntotaldealnum = isnull(ntotaldealnum,0) + ? where cinvoice_bid = ?";
					stmt = con.prepareStatement(sql);
					stmt.clearParameters();
					stmt.setDouble(1, dealvo[i].getNwriteinvoicemny().doubleValue());
					stmt.setDouble(2, dealvo[i].getNwriteinvoicenum().doubleValue());
					stmt.setString(3, dealvo[i].getCsourcebillrowid());
					stmt.executeUpdate();
					//sql = "update so_taxinvoice_bb1 set dr = 1 where ctaxinvoice_bbid = ?";
					sql = "insert into so_taxinvoice_bb1 (cdealoper ,csourcebillid , csourcebillrowid , csourcebilltype , ctaxinvoice_bbid , " +
							"ctaxinvoice_bid , ctaxinvoiceid , ddealdate , ndealmny , ndealnum , nwriteinvoicemny , nwriteinvoicenum) " +
							"values(? ,? , ? , ? , ? ," +
							"? , ? , ? , ? , ? , ? , ?)";
					stmt = con.prepareStatement(sql);
					stmt.clearParameters();
					stmt.setString(1, dealvo[i].getCdealoper());
					stmt.setString(2, dealvo[i].getCsourcebillid());
					stmt.setString(3, dealvo[i].getCsourcebillrowid());
					stmt.setString(4, dealvo[i].getCsourcebilltype());
					stmt.setString(5, getOID());
					stmt.setString(6, dealvo[i].getCtaxinvoice_bid());
					stmt.setString(7, dealvo[i].getCtaxinvoiceid());
					stmt.setString(8, dealvo[i].getDdealdate().toString());
					stmt.setDouble(9, dealvo[i].getNdealmny().doubleValue());
					stmt.setDouble(10, dealvo[i].getNdealnum().doubleValue());
					stmt.setDouble(11, dealvo[i].getNwriteinvoicemny().doubleValue());
					stmt.setDouble(12, dealvo[i].getNwriteinvoicenum().doubleValue());
					stmt.executeUpdate();
					
				}
				//stmt.executeBatch();
				con.commit();
			
			} catch (SQLException e) {
				con.rollback();
				throw new SQLException(e.getMessage(), e.getSQLState());
			} finally {
				try {
					if (stmt != null) {
						stmt.close();
					}
				} catch (Exception e) {
				}
				try {
					if (con != null) {
						con.close();
					}
				} catch (Exception e) {
				}
			}
	}
	
	public  void deleteDeal(TaxInvoiceBbVO[] dealvo) throws SQLException {
//		PersistenceManager sessionManager=null;
		CrossDBConnection con = null;
		PreparedStatement stmt = null;
		String sql = null;
		try {
//			sessionManager = PersistenceManager.getInstance ();
//			JdbcSession session = sessionManager. getJdbcSession ();

			con = (CrossDBConnection) this.getConnection(); 
			con.setAutoCommit(false);
			for(int i=0; i<dealvo.length; i++){
				sql = "update so_taxinvoice_b  set ntotaldealmny = ntotaldealmny - ?, ntotaldealnum = ntotaldealnum - ? where ctaxinvoice_bid = ?";
				stmt = con.prepareStatement(sql);
				stmt.setDouble(1, dealvo[i].getNdealmny().doubleValue());
				stmt.setDouble(2, dealvo[i].getNdealnum().doubleValue());
				stmt.setString(3, dealvo[i].getCtaxinvoice_bid());
				stmt.executeUpdate();
				sql = "update so_saleinvoice_b set ntotaldealmny = ntotaldealmny - ?, ntotaldealnum = ntotaldealnum - ? where cinvoice_bid = ?";
				stmt = con.prepareStatement(sql);
				stmt.setDouble(1, dealvo[i].getNwriteinvoicemny().doubleValue());
				stmt.setDouble(2, dealvo[i].getNwriteinvoicenum().doubleValue());
				stmt.setString(3, dealvo[i].getCsourcebillrowid());
				stmt.executeUpdate();
				//sql = "update so_taxinvoice_bb1 set dr = 1 where ctaxinvoice_bbid = ?";
				//sql = "update so_taxinvoice_bb1 set dr = 1 where ctaxinvoice_bbid = ?";
				sql = "delete from so_taxinvoice_bb1  where ctaxinvoice_bbid = ?";
				stmt = con.prepareStatement(sql);
				stmt.setString(1, dealvo[i].getCtaxinvoice_bbid());
				stmt.executeUpdate();

			}
			//session.executeBatch();
			//stmt.executeBatch();
			con.commit();
		} catch (SQLException e) {
			//con.rollback();
			throw new SQLException(e.getMessage(), e.getSQLState());
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception e) {
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
			}
		}
	}
}
