/*
 * Created on 15/11/2006
 *
 */
package com.fqueue.commons;

import java.util.HashMap;

/**
 * @author atirta
 *
 */
public class SnsFunctionCode {
	public static final String AcctPowerfind = "A110";	
	public static final String AcctListViewSelection = "A120";	
	public static final String AcctListViewWithoutBal = "A130";	
	public static final String AcctListValidationStage = "A140";	
	public static final String AcctListViewWithBal = "A150";	
	public static final String MyAllAccts = "A160";	
	public static final String AcctSummarySavingsO = "A210";	
	public static final String AcctSummaryGiro = "A220";	
	public static final String AcctMaintenance = "A310";
	public static final String DormantAcctActivation = "A315";
	public static final String AcctClosing = "A320";
	/** Account Change Billing Address screen code=A36*/
	public static final String AcctChangesBillingAddress = "A330";
	public static final String AcctHapusMessage = "A340";
	public static final String AcctEditCustMessage = "A350";
	public static final String AcctEditInitialDeposit = "A360";
	
	/** Account creation untuk semua produk */
	public static final String AcctCreation = "A410";
	/** Account detail untuk produk Giro */
	public static final String AcctDetailsGiro = "A420";
	/** Account detail untuk produk Saving */
	public static final String AcctDetailsSaving = "A430";	
	public static final String RemoteSupervisorOverride = "A450";	
	public static final String CardAndPINCreation = "A460";
	public static final String CardMaintenance = "A461";
	/** Account detail balance page view, ePoint screen code=A06 */
	public static final String AcctDetailBalView = "A510";	
	/** Account balance hold amount list, ePoint screen code=A06c */
	public static final String AcctBalHoldAmtList = "A520";	
	/** Account balance hold amount page, ePoint screen code=A06a */
	public static final String AcctBalHoldAmt = "A530";	
	public static final String AcctBalReleaseHoldAmtPage = "A540";	
	public static final String AcctBalEditHoldAmtPage = "A550";	
	public static final String AcctDetailViewMutasiList = "A560";	
	public static final String AcctGiroOverdraftPage = "A570";	
	public static final String AcctDetailGiroCerukanPage = "A580";
	
	public static final String CustPowerfind = "C110";	
	public static final String CustListView = "C120";
	/** Customer Individual Detail View - Customer Information Tab, ePoint screen code=C03 */
	public static final String CustIndDetailView = "C130";	
	/** Customer Individual Detail View long CIF, ePoint screen code=C03a */
	public static final String CustIndDetailViewLong = "C140";	
	/** Customer Corporate Detail View - Customer Information Tab, ePoint screen code=C04 */
	public static final String CustCorpDetailView = "C150";	
	/** Customer Corporate Detail View Long CIF , ePoint screen code=C04a */
	public static final String CustCorpDetailViewLong= "C160";	
	public static final String IncompleteMandatoryLongCif = "C170";	
	public static final String IncompleteOptionalLongCif = "C180";	
	/** Create short CIF for Individual Customer, ePoint screen code=C05 */
	public static final String CustIndCreateShortCIF = "C210";	
	/** Create Long CIF for Individual Customer, ePoint screen code=C06 */
	public static final String CustIndCreateLongCIF = "C220";	
	/** Create short CIF for Corporate Customer, ePoint screen code=C07 */
	public static final String CustCorpCreateShortCIF = "C230";	
	/** Create Long CIF for Corporate Customer, ePoint screen code=C08 */
	public static final String CustCorpCreateLongCIF = "C240";	
	/** Edit Customer Individual Page - Personal Information screen code=C09 
	 * <p>Class name=CustomerIndInfoEdit, Function Code=C310	*/
	public static final String EditCustIndPersonalInfo = "C310";
	/** <p>Edit Customer Individual Page - Address Information	screen code=C09a 
	 * <p>Class name=CustomerIndAddressEdit, Function Code=C320	*/
	public static final String EditCustIndAddress = "C320";	
	/** Edit Customer Corporate Page - Company Information	screen code=C11 
	 * <p>Class name=CustomerCorpInfoEdit, Function Code=C330	*/
	public static final String EditCustCorpInfo = "C330";	
	/** Edit Customer Corporate Page - Address Information	screen code=C11a 
	 * <p>Class name=CustomerCorpAddressEdit, Function Code=C340	*/
	public static final String EditCustCorpAddress = "C340";
	/** Edit Customer Individual Optional Field	screen code=C25 
	 * <p>Class name=CustomerIndOptionalField, Function Code=C350	*/	
	public static final String EditCustIndOptionalField = "C350";
	/** Edit Customer Corporate Optional Field screen code=C26 
	 * <p>Class name=CustomerCorpOptionalField, Function Code=C360*/	
	public static final String EditCustCorpOptionalField = "C360";
	/** Edit Customer Corporate Optional Field screen code=C365 
	 * <p>Class name=CustomerCorpOptionalField, Function Code=C365*/	
	public static final String EditCustomerCorpManagement = "C365";
	/** Edit Retour / VIP Maintenance screen code=C28a 
	 * 	<p>Class name=CustomerRetourEdit, Function Code=C370 */	
	public static final String RetourVIPMaintenance = "C370";	
	public static final String ViewNamaIbuKandung = "C380";
	public static final String ViewCustomerFacilities = "C381";
	/** Edit VIP Code screen code=C28c 
	 * <p>Class name=CustomerVIPCodeEdit, Function Code=C390 */	
	public static final String EditVIPCode = "C390";
	/** Edit CIF Statement Cycle screen code=C28d 
	 * <p>Class name=CustomerCIFStmtCycle, Function Code=C3A0 */
	public static final String EditCIFStatementCycle = "C3A0";
	/** Edit Hold Mail screen code=C28e 
	 * <p>Class name=CustomerHoldMail, Function Code=C3B0 */
	public static final String EditHoldMail = "C3B0";	
	public static final String CustAdditionalAddress = "C3C0";
	public static final String CustAdditionalPhone = "C3E0";	
	public static final String CustAddressCreation = "C3D0";	
//	public static final String CustPhoneCreation = "C3E0";
	/** Delete CIF Message screen code=A41/A41A */
	public static final String HapusMessageCIF = "C3F0";
	/** Edit CIF Message screen code=A42/A42A */
	public static final String EditCustMessageCIF = "C3G0";	
	public static final String AcctMaintainCardPage = "K110";	
	public static final String CreateCard = "K210";	
	public static final String CreatePIN_CardReset = "K310";	
	public static final String CardChangePIN = "K320";	
	public static final String ChangeCard = "K330";	
	public static final String ChangeCardPIN = "K340";	
	public static final String BlockCardPage = "K350";	
	public static final String UnBlockCardPage = "K360";	
	public static final String CloseCardPage = "K370";	
	public static final String CloseCardPIN = "K380";	
	public static final String ProspectListView = "X120";	
	public static final String ProspectIndDetailView = "X130";	
	public static final String ProspectCorpDetailView = "X140";	
	public static final String CreateProspectInd = "X210";	
	public static final String CreateProspectCorp = "X220";	
	public static final String EditProspectInd = "X310";	
	public static final String EditProspectCorp = "X320";
	public static final String StackServices = "Q999";
	public static final String SplitIndividual = "C700";
	public static final String SplitCorporate = "C710";
	public static final String CompareCorporate = "C650";
	public static final String CompareIndividual = "C660";
	
	public static final String AccountPassbookMaintenance = "APBM001";	//function code for account passbook maintenance
	
	
	private HashMap scrcode=null;
	
	public SnsFunctionCode(){
		initMap();
	}
	
	private void initMap(){
		scrcode=new HashMap();
		scrcode.put(EditCustIndPersonalInfo, "C09");
		scrcode.put(EditCustIndAddress, "C09A");
		scrcode.put(EditCustCorpInfo, "C11");
		scrcode.put(EditCustCorpAddress, "C11A");
		scrcode.put(EditCustIndOptionalField, "C25");
		scrcode.put(EditCustCorpOptionalField, "C26");
		scrcode.put(RetourVIPMaintenance, "C28A");
		scrcode.put(EditVIPCode, "C28C");
		scrcode.put(EditCIFStatementCycle, "C28D");
		scrcode.put(EditHoldMail, "C28E");
		scrcode.put(HapusMessageCIF, "A41");
		scrcode.put(HapusMessageCIF+"Account" , "A41A");
		scrcode.put(EditCustMessageCIF, "A42");
		scrcode.put(EditCustMessageCIF+"Account", "A42A");
//		scrcode.put("", "A12B");	// Time deposit maintenance
//		scrcode.put("", "A12D");	// Time deposit maintenance
//		scrcode.put("", "A12E");	// Time deposit maintenance
		scrcode.put(AcctChangesBillingAddress, "A36");
		scrcode.put(CustAdditionalAddress, "C22");
		scrcode.put(SplitIndividual, "C700");
		scrcode.put(SplitCorporate, "C710");
		scrcode.put(CompareCorporate, "C650");
		scrcode.put(CompareIndividual, "C660");
		
	}
	/**
	 * Get screen code for batch data transfer to JHA
	 * @param functionCode
	 * @return the Screen Code 
	 */
	public String getScreenCode(String functionCode){
		return (String)scrcode.get(functionCode);
	}
	
	public static void main(String[] args) {
	}
}
