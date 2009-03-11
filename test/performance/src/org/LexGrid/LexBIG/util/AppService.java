package org.LexGrid.LexBIG.util;


import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Extensions.Generic.LexBIGServiceConvenienceMethods;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.util.Prompt;

public class AppService {
    private static AppService _instance = null;
    private CodingSchemeSummary _css = null;
    private static LexBIGService _lbSvc = null;
    private String _scheme = "";
    private CodingSchemeVersionOrTag _csvt = null;
    private LexBIGServiceConvenienceMethods _lbscm = null;
    
    private AppService() {
        _css = Util.promptForCodeSystem();
        _lbSvc = getLBSvc();
        _scheme = _css.getCodingSchemeURN();
        _csvt = new CodingSchemeVersionOrTag();
        _csvt.setVersion(_css.getRepresentsVersion());
    }
    
    public static AppService getInstance() {
        if (_instance == null)
            _instance = new AppService();
        return _instance;
    }
    
    public CodingSchemeSummary getCSS() {
        return _css;
    }
    
    public static LexBIGService getLBSvc() {
        //Note: Moved this code from constructor, to prevent infinite
        //  recursion from occuring.
        if (_lbSvc == null) {
            boolean remote = Prompt.prompt("Remote", false);
            if (remote)
                _lbSvc = RemoteServerUtil2.createLexBIGService();
            else _lbSvc = LexBIGServiceImpl.defaultInstance();
        }
        return _lbSvc;
    }
    
    public String getScheme() {
        return _scheme;
    }
    
    public CodingSchemeVersionOrTag getCSVT() {
        return _csvt;
    }
    
    public LexBIGServiceConvenienceMethods getLBSCM() throws LBException {
        if (_lbscm == null) { 
            _lbscm = (LexBIGServiceConvenienceMethods) 
            _lbSvc.getGenericExtension("LexBIGServiceConvenienceMethods");
            _lbscm.setLexBIGService(_lbSvc);
        }
        return _lbscm;
    }
}
