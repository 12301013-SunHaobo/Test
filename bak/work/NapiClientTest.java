package napi;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.wgen.httpjson.connections.HttpClient3ConnFactory;
import net.wgen.napi.client.CsvData;
import net.wgen.napi.client.NapiBatchClient;
import net.wgen.napi.client.NapiClient;
import net.wgen.napi.client.NapiUtils.RequestFilters;
import net.wgen.spring.common.infrastructure.wsclient.napi.ExternalStaffJSONTranslator;
import net.wgen.spring.common.model.external.ExternalStaff;

public class NapiClientTest {
    
    private static String host = "service-mas-futureqa.mc.wgenhq.net";
    private static String scheme = "https";
    private static int port = 60005;
    private static String username = "312";
    private static String password = "OJ9V51W7dAPc8RFI0BjY";

    
    private static String oibUid = "mtacha72784"; //"amarch","hmister","rking"
    
    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        // getMasToken();
        //testStaffOld();
        //testStaffView();
        testMyInstitutionHierarchyView();
        //testBatch();
    }

    private static void testStaffOld() throws Exception {
        NapiClient nc = getNapiClient();
        
        //20420537 , has 144 results
        //433896614, has 3 results
        JSONArray staffView = nc.getStaffInfo("20420537", 2012); //
        ExternalStaffJSONTranslator translator = new ExternalStaffJSONTranslator();
        
        for(Object o : staffView) {
            ExternalStaff s = translator.fromJSON((JSONObject)o);
            System.out.println(s.getLastName()+", "+s.getFirstName()+" "+s.getUid()+" institutionName:"+s.getCurrentInstId()+" visables:"+s.getVisibleInsts().toString());
        }
        
        System.out.println(staffView.toString());
        System.out.println("------------------");
        
    }

    
    private static void testStaffView() throws Exception {
        NapiClient nc = getNapiClient();
        
        RequestFilters requestFilters = new RequestFilters();
        Set<String> institutionsIds = new HashSet<String>(Arrays.asList("20425541"));//"433896614","433897440","459575773","467773983"
        requestFilters.addInstitutionUids(institutionsIds);
        //requestFilters.addInstitutionUid("20420623");

        JSONArray staffView = nc.staffView(requestFilters);
        
        for(Object o : staffView) {
//            ExternalStaff s = translator.fromJSON((JSONObject)o);
//            System.out.println(s.getLastName()+", "+s.getFirstName()+" "+s.getUid()+" institutionName:"+s.getCurrentInstId()+" visables:"+s.getVisibleInsts().toString());
        }
        
        System.out.println(staffView.toString());
        System.out.println("------------------");
        
    }
    
    private static void testMyInstitutionHierarchyView() throws Exception {
        NapiClient nc = getNapiClient();
        JSONArray jsonArray = nc.myInstitutionHierarchyView("459066524");
        System.out.println(jsonArray.toString());
            
    }

    static String[] amarchInstUids = {
        "433896614",
        "433897440",
        "459575773",
        "467773983",
    };
    
    static String[] cpsUserInstUids_100 = {
            "20420537",
            "20420623",
    };
    
    //501 institutions 
    static String[] cpsUserInstUids_501 = {
            "20420537",
            "20420623",
            "20425541",
            "20425933",
            "20426059",
            "43467485",
            "43467534",
            "43467558",
            "43467583",
            "43467608",
            "43467634",
            "43467662",
            "43467688",
            "43467712",
            "43467760",
            "43467811",
            "43467861",
            "43467885",
            "43467909",
            "43467969",
            "43468000",
            "43468082",
            "43468113",
            "43699324",
            "43699776",
            "43700596",
            "43700760",
            "43700948",
            "43701230",
            "43701534",
            "43701714",
            "43701878",
            "43702047",
            "43702209",
            "43702539",
            "43702702",
            "43702823",
            "43702902",
            "43703029",
            "43703148",
            "43703241",
            "47932844",
            "206261316",
            "217670146",
            "20420741",
            "20431307",
            "20431661",
            "20432394",
            "20432692",
            "20434180",
            "20435623",
            "20435729",
            "20435957",
            "20436203",
            "23225020",
            "43468138",
            "43468209",
            "43468238",
            "43468265",
            "43468289",
            "43468313",
            "43468337",
            "43468361",
            "43468385",
            "43468409",
            "43468433",
            "43468457",
            "43468481",
            "43489274",
            "43705059",
            "43705107",
            "43705131",
            "43705370",
            "43705456",
            "43705503",
            "43705527",
            "43705622",
            "43705717",
            "43705844",
            "43705914",
            "43705975",
            "43706023",
            "43706071",
            "20420816",
            "20437592",
            "20438101",
            "20438337",
            "20438426",
            "20438546",
            "20438682",
            "20438798",
            "20440760",
            "20440934",
            "28777217",
            "43468505",
            "43468529",
            "43468558",
            "43468582",
            "43514227",
            "43706095",
            "43706121",
            "43706149",
            "43706224",
            "43706249",
            "43706278",
            "20420836",
            "20441105",
            "20441265",
            "20441339",
            "20441969",
            "20454818",
            "20455010",
            "20455131",
            "43468887",
            "43468939",
            "43468977",
            "43469001",
            "43469025",
            "43488957",
            "43488981",
            "43706348",
            "43706376",
            "43706404",
            "43706437",
            "43706474",
            "43706501",
            "43706582",
            "43706607",
            "43706635",
            "43706750",
            "43706822",
            "43706987",
            "362369232",
            "20422244",
            "20454039",
            "20454497",
            "20456901",
            "20458966",
            "20459216",
            "43469073",
            "43469162",
            "43469186",
            "43469276",
            "43469405",
            "43469429",
            "43489008",
            "43489076",
            "43489161",
            "43489185",
            "43489298",
            "43706726",
            "43706774",
            "43706847",
            "43706872",
            "43706913",
            "43706937",
            "43706961",
            "43707095",
            "43707121",
            "43707169",
            "43707209",
            "43707280",
            "43707346",
            "43707371",
            "52323943",
            "152732072",
            "206265284",
            "20423728",
            "20456385",
            "20456689",
            "20461253",
            "20461597",
            "20462075",
            "20462312",
            "20478302",
            "43469049",
            "43469596",
            "43469622",
            "43469659",
            "43469709",
            "43469733",
            "43489336",
            "43489434",
            "43489530",
            "43707524",
            "43707567",
            "43707758",
            "43707806",
            "43707859",
            "43708012",
            "43708193",
            "43708217",
            "43708573",
            "43708648",
            "43708696",
            "43708767",
            "91676218",
            "221565350",
            "20424586",
            "20465448",
            "20466829",
            "20467257",
            "20467455",
            "20478326",
            "20480979",
            "20481295",
            "20481663",
            "20482222",
            "20484096",
            "20485649",
            "20485833",
            "20486245",
            "29457964",
            "43469848",
            "43470029",
            "43470092",
            "43470129",
            "43470153",
            "43470292",
            "43470340",
            "43470630",
            "43489679",
            "43489731",
            "43513997",
            "43703963",
            "43708165",
            "43708243",
            "43708324",
            "43708376",
            "43708510",
            "43708539",
            "43708597",
            "43708598",
            "43708793",
            "43709048",
            "362368302",
            "20424650",
            "20465734",
            "20465963",
            "20477175",
            "20485394",
            "20487526",
            "20487967",
            "20488258",
            "20488364",
            "20488602",
            "20489574",
            "28777891",
            "43470005",
            "43470737",
            "43473330",
            "43473340",
            "43473372",
            "43473403",
            "43473453",
            "43473489",
            "43489774",
            "43489837",
            "43490105",
            "43703363",
            "43703410",
            "43703481",
            "43703529",
            "43703580",
            "43703604",
            "43703655",
            "43708720",
            "20424675",
            "20487147",
            "20487358",
            "20487573",
            "20570782",
            "43473560",
            "43473585",
            "43473609",
            "43473652",
            "43488078",
            "43498335",
            "43498359",
            "43498394",
            "43498418",
            "43498419",
            "43498444",
            "43514303",
            "43703750",
            "43703821",
            "43703868",
            "43703915",
            "43704034",
            "43704081",
            "43704293",
            "43704364",
            "43704412",
            "48370809",
            "55956999",
            "20424736",
            "20490700",
            "20490777",
            "20492159",
            "20492338",
            "20492460",
            "20492789",
            "20493073",
            "20493420",
            "20493791",
            "23763112",
            "29457409",
            "43470631",
            "43470655",
            "43470809",
            "43470833",
            "43470834",
            "43704604",
            "43704628",
            "43704652",
            "43704700",
            "43704724",
            "43704748",
            "43704772",
            "43704796",
            "43704844",
            "43704892",
            "43704940",
            "51064333",
            "85390411",
            "20424744",
            "20488603",
            "20494691",
            "20494786",
            "20495078",
            "20495145",
            "20495237",
            "20495621",
            "20495902",
            "20496031",
            "20496146",
            "20496175",
            "23225037",
            "29456545",
            "29456622",
            "29456623",
            "29456636",
            "29457032",
            "29457123",
            "43470679",
            "43470761",
            "43470835",
            "43470859",
            "43498468",
            "43703316",
            "43703434",
            "43703505",
            "43703556",
            "43703628",
            "20424748",
            "20506072",
            "20506356",
            "20506627",
            "20507132",
            "20507941",
            "20508015",
            "20508235",
            "28778011",
            "43470580",
            "43470899",
            "43470923",
            "43470947",
            "43470971",
            "43499422",
            "43499449",
            "43704868",
            "43704999",
            "43705083",
            "43706047",
            "43706179",
            "45929337",
            "130678856",
            "362366296",
            "20424759",
            "20508641",
            "20508854",
            "20509130",
            "20509885",
            "28777630",
            "43469783",
            "43469811",
            "43469885",
            "43469909",
            "43469933",
            "43469957",
            "43488005",
            "43499511",
            "43499613",
            "43703679",
            "43703703",
            "43703774",
            "43703939",
            "43704010",
            "43704151",
            "43704175",
            "43704246",
            "43704317",
            "43704388",
            "43704437",
            "43704484",
            "43704508",
            "43704532",
            "43704556",
            "43704580",
            "20424766",
            "20510183",
            "20510504",
            "20511516",
            "20511931",
            "20512354",
            "20512468",
            "20512799",
            "20514001",
            "20514298",
            "20515000",
            "20515406",
            "28777731",
            "28778041",
            "43470177",
            "43470316",
            "43470364",
            "43470388",
            "43499637",
            "43499661",
            "43499711",
            "43705180",
            "43705233",
            "43705280",
            "43705397",
            "43705551",
            "43705598",
            "43705646",
            "43705693",
            "43705749",
            "43705796",
            "43705843",
            "48372589",
            "51055854",
            "20424813",
            "20497414",
            "20497469",
            "20497494",
            "20497522",
            "20497546",
            "20497572",
            "20497598",
            "29457364",
            "29457854",
            "43470436",
            "43470484",
            "43470508",
            "43470532",
            "43470604",
            "43470628",
            "43499773",
            "43499810",
            "43499834",
            "43499910",
            "43499934",
            "43514330",
            "43707396",
            "43707500",
            "43707686",
            "43707710",
            "43707734",
            "43707895",
            "43707920",
            "43707948",
            "43708036",
            "43708060",
            "130677887",
            "82497734",
            "338662160",
            "217671058",
            "217671322",
            "362364866",
            "362364868",
            "362364870",
            "362364872",
            "362383088",
            "20464708",
            "29457557",
            "362386200",
            "362384546",
            "20467109",
            "362386476",
            "362386478",
            "365051896",
            "20488485",
            "28777424",
            "365055924",
            "20488222",
            "43470703",
            "43470785",
            "43498516",
            "43704676",      
    };
    
    private static NapiClient getNapiClient() throws Exception{
        String authToken = TestNapiUtil.getMasToken(oibUid); //"ekV5Rk4wMlgvWFA4Tkg0eDdEZDZNZUxscTJqc21HX0N4eTBrVWJLWW9iSF9JZFVNbjNhd1NRcC1F";

        HttpClient3ConnFactory httpClient3ConnFactory = new HttpClient3ConnFactory(
                scheme, host, port, username,
                password, 1);

        NapiClient nc = new NapiClient(httpClient3ConnFactory.getAuthenticatedConnection(authToken));
        return nc;
    }

    
    private static void testBatch() throws Exception {
        String authToken = TestNapiUtil.getMasToken(oibUid); //"ekV5Rk4wMlgvWFA4Tkg0eDdEZDZNZUxscTJqc21HX0N4eTBrVWJLWW9iSF9JZFVNbjNhd1NRcC1F";

        HttpClient3ConnFactory httpClient3ConnFactory = new HttpClient3ConnFactory(scheme, host, port, username,
                password, 20);

        NapiBatchClient nbc = new NapiBatchClient(httpClient3ConnFactory.getAuthenticatedConnection(authToken));
        RequestFilters requestFilters = new RequestFilters();
        //requestFilters.addStaffUid("123");
        //requestFilters.addInstitutionUid("433896614");433897440
        Set<String> instUids = new HashSet<String>(Arrays.asList(new String[]{"433896614","433897440","459575773","467773983"}));
        requestFilters.addInstitutionUids(instUids);
        
        CsvData cd = nbc.batchInstitutionStudentView(requestFilters);
        for(int i=0;i<cd.getNumRows();i++){
            System.out.println(cd.getRowData(0, i)+":"+cd.getRowData(1, i));
        }
        System.out.println(cd.getNumRows());
        
    }

    
}
