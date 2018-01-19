package model_classes;

public class Category {
    public String getAccountID() {
        return accountID;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }

    public String getAccountNumber() {
        return AccountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        AccountNumber = accountNumber;
    }

    public String getSecuritiesName() {
        return SecuritiesName;
    }

    public void setSecuritiesName(String securitiesName) {
        SecuritiesName = securitiesName;
    }

    public String getSymbol() {
        return Symbol;
    }

    public void setSymbol(String symbol) {
        Symbol = symbol;
    }

    public String getAllocation() {
        return Allocation;
    }

    public void setAllocation(String allocation) {
        Allocation = allocation;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getCostBasisPrice() {
        return CostBasisPrice;
    }

    public void setCostBasisPrice(String costBasisPrice) {
        CostBasisPrice = costBasisPrice;
    }

    public String getMarketPrice() {
        return MarketPrice;
    }

    public void setMarketPrice(String marketPrice) {
        MarketPrice = marketPrice;
    }

    public String getInvestementValue() {
        return InvestementValue;
    }

    public void setInvestementValue(String investementValue) {
        InvestementValue = investementValue;
    }

    public String getPosValuePerSec() {
        return PosValuePerSec;
    }

    public void setPosValuePerSec(String posValuePerSec) {
        PosValuePerSec = posValuePerSec;
    }

    public String getUpdatedDate() {
        return UpdatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        UpdatedDate = updatedDate;
    }

    public String getUpdatedDateUnix() {
        return UpdatedDateUnix;
    }

    public void setUpdatedDateUnix(String updatedDateUnix) {
        UpdatedDateUnix = updatedDateUnix;
    }

    public String getSecurityDescription() {
        return SecurityDescription;
    }

    public void setSecurityDescription(String securityDescription) {
        SecurityDescription = securityDescription;
    }

    public String getUnrealizedG_LPerSecurity() {
        return UnrealizedG_LPerSecurity;
    }

    public void setUnrealizedG_LPerSecurity(String unrealizedG_LPerSecurity) {
        UnrealizedG_LPerSecurity = unrealizedG_LPerSecurity;
    }

    public boolean is_open() {
        return is_open;
    }

    public void setIs_open(boolean is_open) {
        this.is_open = is_open;
    }

    private  String accountID ="";
    private String AccountNumber="";
    private String SecuritiesName="";
    private String Symbol="";
    private String Allocation="";
    private String Quantity="";
    private String CostBasisPrice="";
    private String MarketPrice="";
    private String InvestementValue="";
    private String PosValuePerSec="";
    private String UpdatedDate="";
    private String UpdatedDateUnix="";
    private String SecurityDescription="";
    private String UnrealizedG_LPerSecurity="";
    private String modelsFkId="";

    public String getApiPrice() {
        return apiPrice;
    }

    public void setApiPrice(String apiPrice) {
        this.apiPrice = apiPrice;
    }

    public String getApichange() {
        return apichange;
    }

    public void setApichange(String apichange) {
        this.apichange = apichange;
    }

    public String getApistatus() {
        return apistatus;
    }

    public void setApistatus(String apistatus) {
        this.apistatus = apistatus;
    }

    private String  apiPrice,apichange,apistatus;

    public String getModelsFkId() {
        return modelsFkId;
    }

    public void setModelsFkId(String modelsFkId) {
        this.modelsFkId = modelsFkId;
    }

    public String getPositionID() {
        return positionID;
    }

    public void setPositionID(String positionID) {
        this.positionID = positionID;
    }

    private String positionID="";
    public String getColor() {
        return color;
    }
    public void setColor(String color) {
        this.color = color;
    }
    private String color="";
    private  boolean is_open;

    public Category(String accountID,String modelsFkId, String positionID, String AccountNumber, String SecuritiesName, String Symbol, String Allocation, String Quantity
            , String CostBasisPrice , String MarketPrice, String InvestementValue, String PosValuePerSec, String UpdatedDate,
                    String UpdatedDateUnix, String SecurityDescription, String UnrealizedG_LPerSecurity,
                    boolean is_open, String color,String apiPrice,String apichange,String apistatus)
    {
        this.accountID = accountID;
        this.AccountNumber = AccountNumber;
        this.SecuritiesName = SecuritiesName;
        this.Symbol = Symbol;
        this.Allocation = Allocation;
        this.Quantity = Quantity;
        this.CostBasisPrice = CostBasisPrice;
        this.MarketPrice = MarketPrice;
        this.InvestementValue = InvestementValue;
        this.PosValuePerSec = PosValuePerSec;
        this.UpdatedDate = UpdatedDate;
        this.UpdatedDateUnix = UpdatedDateUnix;
        this.SecurityDescription = SecurityDescription;
        this.UnrealizedG_LPerSecurity=UnrealizedG_LPerSecurity;
        this.is_open=is_open;
        this.color=color;
        this.modelsFkId=modelsFkId;
        this.positionID=positionID;
        this.apiPrice=apiPrice;
        this.apichange=apichange;
        this.apistatus=apistatus;
    }




}