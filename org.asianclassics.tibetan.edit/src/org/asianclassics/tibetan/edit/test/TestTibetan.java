package org.asianclassics.tibetan.edit.test;

import org.asianclassics.tibetan.edit.TibetanEditor;
import org.asianclassics.tibetan.transcoder.TibetanTranscoder;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;

public class TestTibetan extends Composite {

	private TibetanEditor tibEdit;
	private TibetanTranscoder tibXcdr;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public TestTibetan(Composite parent, int style) {
		super(parent, style);
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				tibEdit.test(e.button);
			}
		});
		setLayout(new FormLayout());
		
		tibEdit = new TibetanEditor(this, SWT.NONE);
		FormData fd_composite = new FormData();
		fd_composite.top = new FormAttachment(0, 10);
		fd_composite.left = new FormAttachment(0, 10);
		fd_composite.right = new FormAttachment(100, -10);
		tibEdit.setLayoutData(fd_composite);
		tibEdit.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		Button btnDebug = new Button(this, SWT.NONE);
		fd_composite.bottom = new FormAttachment(btnDebug, -6);
		FormData fd_btnDebug = new FormData();
		fd_btnDebug.right = new FormAttachment(0, 71);
		fd_btnDebug.top = new FormAttachment(100, -35);
		fd_btnDebug.bottom = new FormAttachment(100, -10);
		fd_btnDebug.left = new FormAttachment(0, 10);
		btnDebug.setLayoutData(fd_btnDebug);
		btnDebug.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				tibEdit.debug();
			}
		});
		btnDebug.setText("Debug");
		
		Button btnFill = new Button(this, SWT.NONE);
		btnFill.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				fullPechaTest();
			}
		});
		FormData fd_btnFill = new FormData();
		fd_btnFill.top = new FormAttachment(tibEdit, 6);
		fd_btnFill.left = new FormAttachment(btnDebug, 6);
		btnFill.setLayoutData(fd_btnFill);
		btnFill.setText("Fill");
		
		Button btnClear = new Button(this, SWT.NONE);
		btnClear.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				tibEdit.setWorkingText("");
			}
		});
		FormData fd_btnClear = new FormData();
		fd_btnClear.bottom = new FormAttachment(btnDebug, 0, SWT.BOTTOM);
		fd_btnClear.left = new FormAttachment(btnFill, 6);
		btnClear.setLayoutData(fd_btnClear);
		btnClear.setText("Clear");

		
	}

	
	public void init() {
		tibXcdr = new TibetanTranscoder();
		tibEdit.init(tibXcdr);
		
//		hintTest2();
		simpleTest();
//		fullPechaTest();
	}
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	public void hintTest() {
		String rt = "PA RLOM SUM SHES MA DANG DAG NYID ";
		tibEdit.setReferenceText(tibXcdr.transcode(rt, TibetanTranscoder.ROMAN));
		String wt = "PA RLOM MED GSUM SHES PA DANG,";
		tibEdit.setWorkingText(wt);

	}
	
	public void hintTest2() {
		String rt = "GSUM SHES PA DANG, ,'KHOR GSUM RNAM PAR DAG NYID DANG, ,SNYING RJE DANG NI RLOM MED DANG, ,CHOS MNYAM NYID DANG TSUL GCIG SHES, ,MI SKYE BA DANG BZOD SHES DANG, ,CHOS RNAMS PA GCIG TU STON, ,RTOG PA KUN TU 'JOMS PA DANG, ,'DU SHES LTA DANG NYON MONGS\n"+
				"SPONG; ,ZHI GNAS NGES PAR SEMS PA DANG, ,LHAG MTHONG LA NI MKHAS PA DANG, ,SEMS DUL BA DANG THAMS CAD LA, ,THOGS PA MED PA'I YE SHES DANG, ,CHAGS PA'I SA MIN GAR 'DOD PAR, ,ZHING GZHAN DU NI MNYAM 'GRO DANG, ,KUN TU BDAG GI NGO BO NI, ,STON PA NYID\n"+
				"DANG NYI SHU'O, ,SEMS CAN KUN YID SHES PA DANG, ,MNGON PAR SHES PAS BRTZE BA DANG, ,SANGS RGYAS ZHING BZANG SGRUB PA DANG, ,YONGS SU BRTAG PHYIR SANGS RGYAS BRTEN, ,DBANG PO SHES DANG RGYAL BA YI, ,ZHING SBYONG SGYU MA LTAR GNAS DANG, ,BSAMS BZHIN SRID PA\n"+
				"LEN PA DANG, ,LAS NI RNAM PA 'DI BRGYAD BSHAD, ,SMON LAM DAG NI MTHA' YAS DANG, ,LHA LA SOGS PA'I SKAD SHES DANG, ,SPOBS PA CHU BO LTA BU DANG, ,MNGAL DU 'JUG PA MCHOG DANG NI, ,RIGS DANG RUS DANG CHO 'BRANG DANG, ,'KHOR DANG SKYE BA DAG DANG NI, ,NGES\n"+
				"'BYUNG BYANG CHUB SHING RNAMS DANG, ,YON TAN BUN SUM TSOGS PA YIN, ,SA DGU 'DAS NAS YE SHES NI, ,GANG GIS SANGS RGYAS SAR GNAS PA, ,DE NI BYANG CHUB SEMS DPA'I SA, ,BCU PA YIN PAR SHES PAR BYA, ,MTHONG DANG SGOM PA'I LAM DAG LA, ,GZUNG\n"+
				"DANG 'DZIN PA'I RNAM RTOG RNAMS, ,NYE BAR ZHI BAR BYA BA'I PHYIR, ,GNYER PO RNAM PA BRGYAD CES BYA, ,CHED DU BYA DANG MNYAM NYID DANG, ,SEMS CAN DON DANG 'BAD MED DANG, ,MTHA' LAS 'DAS PAR NGES BYUNG DANG, ,THOB PA'I MTSAN NYID NGES 'BYUNG DANG, ,RNAM PA\n"+
				"THAMS CAD MKHYEN NYID DANG, ,LAM GYI YUL CAN NGES 'BYUNG STE, ,RNAM PA BRGYAD KYI BDAG NYID 'DI, ,NGES 'BYUNG SGRUB PA YIN ZHES BYA, ,SHES RAB KYI PHA ROL TU PHYIN PA'I MAN NGAG GI BSTAN BCOS MNGON PAR RTOGS PA'I RGYAN GYI TSIG LE'UR BYAS PA LAS, ,SKABS DANG\n";
		
		tibEdit.setReferenceText(tibXcdr.transcode(rt, TibetanTranscoder.ROMAN));
		tibEdit.setWorkingText(rt);

	}
	
	public void simpleTest() {
		String fpt = "PA KA ,DANG, ,";
		tibEdit.setWorkingText(fpt);
	}
	
	
	public void fullPechaTest() {
		String fpt = "GSUM SHES PA DANG, ,'KHOR GSUM RNAM PAR DAG NYID DANG, ,SNYING RJE DANG NI RLOM MED DANG, ,CHOS MNYAM NYID DANG TSUL GCIG SHES, ,MI SKYE BA DANG BZOD SHES DANG, ,CHOS RNAMS PA GCIG TU STON, ,RTOG PA KUN TU 'JOMS PA DANG, ,'DU SHES LTA DANG NYON MONGS\n"+
				"SPONG; ,ZHI GNAS NGES PAR SEMS PA DANG, ,LHAG MTHONG LA NI MKHAS PA DANG, ,SEMS DUL BA DANG THAMS CAD LA, ,THOGS PA MED PA'I YE SHES DANG, ,CHAGS PA'I SA MIN GAR 'DOD PAR, ,ZHING GZHAN DU NI MNYAM 'GRO DANG, ,KUN TU BDAG GI NGO BO NI, ,STON PA NYID\n"+
				"DANG NYI SHU'O, ,SEMS CAN KUN YID SHES PA DANG, ,MNGON PAR SHES PAS BRTZE BA DANG, ,SANGS RGYAS ZHING BZANG SGRUB PA DANG, ,YONGS SU BRTAG PHYIR SANGS RGYAS BRTEN, ,DBANG PO SHES DANG RGYAL BA YI, ,ZHING SBYONG SGYU MA LTAR GNAS DANG, ,BSAMS BZHIN SRID PA\n"+
				"LEN PA DANG, ,LAS NI RNAM PA 'DI BRGYAD BSHAD, ,SMON LAM DAG NI MTHA' YAS DANG, ,LHA LA SOGS PA'I SKAD SHES DANG, ,SPOBS PA CHU BO LTA BU DANG, ,MNGAL DU 'JUG PA MCHOG DANG NI, ,RIGS DANG RUS DANG CHO 'BRANG DANG, ,'KHOR DANG SKYE BA DAG DANG NI, ,NGES\n"+
				"'BYUNG BYANG CHUB SHING RNAMS DANG, ,YON TAN BUN SUM TSOGS PA YIN, ,SA DGU 'DAS NAS YE SHES NI, ,GANG GIS SANGS RGYAS SAR GNAS PA, ,DE NI BYANG CHUB SEMS DPA'I SA, ,BCU PA YIN PAR SHES PAR BYA, ,MTHONG DANG SGOM PA'I LAM DAG LA, ,GZUNG\n"+
				"DANG 'DZIN PA'I RNAM RTOG RNAMS, ,NYE BAR ZHI BAR BYA BA'I PHYIR, ,GNYER PO RNAM PA BRGYAD CES BYA, ,CHED DU BYA DANG MNYAM NYID DANG, ,SEMS CAN DON DANG 'BAD MED DANG, ,MTHA' LAS 'DAS PAR NGES BYUNG DANG, ,THOB PA'I MTSAN NYID NGES 'BYUNG DANG, ,RNAM PA\n"+
				"THAMS CAD MKHYEN NYID DANG, ,LAM GYI YUL CAN NGES 'BYUNG STE, ,RNAM PA BRGYAD KYI BDAG NYID 'DI, ,NGES 'BYUNG SGRUB PA YIN ZHES BYA, ,SHES RAB KYI PHA ROL TU PHYIN PA'I MAN NGAG GI BSTAN BCOS MNGON PAR RTOGS PA'I RGYAN GYI TSIG LE'UR BYAS PA LAS, ,SKABS DANG\n";
		
		tibEdit.setWorkingText(fpt);
	}
	
	
	
}
