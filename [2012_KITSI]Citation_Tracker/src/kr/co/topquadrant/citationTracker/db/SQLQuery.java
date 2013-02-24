/**
 * 
 */
package kr.co.topquadrant.citationTracker.db;

/**
 * @author coreawin
 * @sinse 2012. 11. 28. 
 * @version 1.0
 * @history 2012. 11. 28. : 최초 작성 <br>
 *
 */
public class SQLQuery {
	
	public static final String CITATION_TYPE = "" +
			"	select citation_type, description from SCOPUS_CITATION_TYPE ";
	
	public class Worker1{
		public static final String FORWARD = "" +
				"	SELECT sd.eid, sd.publication_year, sc.CIT_EID, sd2.publication_year" +
				"	FROM SCOPUS_DOCUMENT sd, SCOPUS_CITATION sc, SCOPUS_DOCUMENT sd2" +
				"	WHERE sd.eid IN (%s)" +
				"	AND sd.eid = sc.eid AND sc.cit_eid = sd2.EID" +
				"	AND sd2.PUBLICATION_YEAR BETWEEN ? AND ?";
		
		public static final String BACKWARD = "" +
				"	SELECT sd.eid, sd.publication_year, sr.REF_EID, sr.PUBLICATION_YEAR" +
				"	FROM SCOPUS_DOCUMENT sd, SCOPUS_REFERENCE sr" +
				"	WHERE sd.eid IN (%s)" +
				"	AND sd.eid = sr.eid" + 
				"	AND sd.PUBLICATION_YEAR BETWEEN ? AND ?";
	}
	
	public class Worker2{
		public static final String TOTAL_CITATION = "" +
		"	SELECT eid, COUNT(cit_eid) FROM SCOPUS_CITATION WHERE eid IN (%s)" +
		"	GROUP BY EID";
		
		public static final String TOTAL_REFERENCE = "" +
		"	SELECT eid, COUNT(ref_eid) FROM SCOPUS_REFERENCE WHERE eid IN (%s)" +
		"	GROUP BY EID";
	}
	
	public class Worker3{
		
		public static final String REFERENCE_OF_REFERENCE = "" +
		"	SELECT sr.eid as eid, sr.ref_eid as refeid, sr2.REF_EID as refrefeid" +
		"	FROM SCOPUS_REFERENCE sr, SCOPUS_REFERENCE sr2, SCOPUS_DOCUMENT sd" +
		"	WHERE sr.eid IN (%s)" +
		"	AND sr.REF_EID = sr2.EID" +
		"	AND sd.eid = sr.eid" +
		"	AND sd.PUBLICATION_YEAR BETWEEN ? AND ?";
		
		public static final String REFERENCE = "" +
		"	SELECT ref_eid FROM SCOPUS_REFERENCE WHERE eid IN (%s)";
		
		public static final String FORWARD_EXTENSION = "" +
		"	SELECT eid" +
		"	FROM SCOPUS_CITATION " +
		"	WHERE eid IN (SELECT DISTINCT cit_eid FROM SCOPUS_CITATION WHERE eid in (%s)";
		
		public static final String BACKWARD_EXTENSION = "" +
		"	SELECT eid" +
		"	FROM SCOPUS_REFERENCE " +
		"	WHERE eid IN (SELECT DISTINCT ref_eid FROM SCOPUS_REFERENCE WHERE eid in (%s)";
	}
	
	public class Worker4{
		public static final String JOURNAL_ALL = "" +
		"		SELECT eid FROM SCOPUS_DOCUMENT " +
		"		WHERE (citation_type, source_id) IN " +
		"			(" +
		"				SELECT distinct citation_type, source_id " +
		"				FROM SCOPUS_DOCUMENT " +
		"				WHERE eid IN (%s) AND PUBLICATION_YEAR BETWEEN ? AND ?" +
		"			)";
		
		public static final String FORWARD_YEAR_STAT_JOURNAL_ALL = "" +
		"	SELECT sc.eid, COUNT(cit_eid) AS cnt, sd.PUBLICATION_YEAR" +
		"	FROM SCOPUS_CITATION sc, SCOPUS_DOCUMENT sd" +
		"	WHERE" +
		"	sc.eid IN(" +
		"		SELECT eid FROM SCOPUS_DOCUMENT " +
		"		WHERE (citation_type, source_id) IN " +
		"			(SELECT distinct citation_type, source_id FROM SCOPUS_DOCUMENT WHERE eid IN (%s) AND PUBLICATION_YEAR BETWEEN ? AND ?)" +
		"	)" +
		"	AND sc.CIT_EID = sd.eid" +
		"	GROUP BY ROLLUP (sc.eid, PUBLICATION_YEAR)" +
		"	HAVING PUBLICATION_YEAR IS NOT NULL " +
		"	ORDER BY eid, PUBLICATION_YEAR";
		
		public static final String BACKWARD_YEAR_STAT_JOURNAL_ALL = "" +
		"	SELECT sd.eid, sd.PUBLICATION_YEAR AS syear, ROUND(AVG(sr.publication_year),2) AS rmeanyear" +
		"	FROM SCOPUS_REFERENCE sr, SCOPUS_DOCUMENT sd" +
		"	WHERE sd.eid IN(" +
		"		SELECT eid FROM SCOPUS_DOCUMENT " +
		"		WHERE (citation_type, source_id) IN " +
		"			(SELECT distinct citation_type, source_id FROM SCOPUS_DOCUMENT WHERE eid IN (%s) AND PUBLICATION_YEAR BETWEEN ? AND ?)" +
		"	)" +
		"	AND sr.eid = sd.eid" +
		"	AND sr.PUBLICATION_YEAR <> 'null'" +
		"	GROUP BY sd.eid, sd.PUBLICATION_YEAR";
		
		public static final String FORWARD_YEAR_STAT = "" +
		"	SELECT sc.eid, COUNT(cit_eid) AS cnt, sd.PUBLICATION_YEAR" +
		"	FROM SCOPUS_CITATION sc, SCOPUS_DOCUMENT sd" +
		"	WHERE sc.eid IN(%s)" +
		"	AND sc.CIT_EID = sd.eid" +
		"	AND sd.PUBLICATION_YEAR BETWEEN ? AND ?"+
		"	GROUP BY ROLLUP (sc.eid, PUBLICATION_YEAR)" +
		"	HAVING PUBLICATION_YEAR IS NOT NULL" +
		"	ORDER BY eid, PUBLICATION_YEAR";
		
		public static final String BACKWARD_YEAR_STAT= "" +
		"	SELECT sd.eid, sd.PUBLICATION_YEAR AS syear, ROUND(AVG(sr.publication_year),2) AS rmeanyear" +
		"	FROM SCOPUS_REFERENCE sr, SCOPUS_DOCUMENT sd" +
		"	WHERE sd.eid IN(%s)" +
		"	AND sr.eid = sd.eid" +
		"	AND sr.PUBLICATION_YEAR <> 'null'" +
		"	AND sd.PUBLICATION_YEAR BETWEEN ? AND ?"+
		"	GROUP BY sd.eid, sd.PUBLICATION_YEAR";
		
		
		public static final String DOCUMENT_INFO= "" +
		"	SELECT 	eid, publication_year, " +
		"			(SELECT replace(source_title, ',', ' ') FROM SCOPUS_SOURCE_INFO si WHERE si.SOURCE_ID=sd.SOURCE_ID) AS source_title, " +
		"			citation_type" +
		"	FROM SCOPUS_DOCUMENT sd" +
		"	WHERE eid IN (%s)";
		
	}
	
	public class Worker5{
		/**
		 * A와 B 논문사이의 Path가 1인 edge 개수.
		 */
		public static final String PATH1 = "" +
				"	SELECT c.eid AS source1, ref_eid AS source2, COUNT(cit_eid) AS path" +
				"	FROM SCOPUS_CITATION c, SCOPUS_REFERENCE r" +
				"	WHERE c.CIT_EID = r.EID" +
				"	AND c.EID = ? AND r.REF_EID = ?" +
				"	GROUP BY c.eid, ref_eid";
		
		public static final String PATH2 = "" +
				"	SELECT c.eid AS source1, r.eid AS source2, COUNT(cit_eid) AS path" +
				"	FROM " +
				"		(" +
				"		SELECT sc1.eid AS eid, sc2.CIT_EID AS cit_eid" +
				"		FROM SCOPUS_CITATION sc1, SCOPUS_CITATION sc2" +
				"		WHERE sc1.eid = ? " +
				"		AND sc1.CIT_EID = sc2.EID" +
				"	) c, " +
				"	SCOPUS_REFERENCE r	" +
				"	WHERE c.CIT_EID = r.REF_EID" +
				"	AND r.eid = ?" +
				"	GROUP BY c.eid, r.eid";
	}
}
