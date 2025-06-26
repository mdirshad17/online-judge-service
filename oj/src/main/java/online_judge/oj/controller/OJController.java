package online_judge.oj.controller;


import online_judge.oj.model.CodeRequestModel;
import online_judge.oj.service.OJService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/submit")
public class OJController {

    @Autowired
    private OJService ojService;
    public OJController(OJService ojService){
        this.ojService=ojService;
    }

    @PostMapping("/run")
    public ResponseEntity<String> submitAndRun(@RequestBody CodeRequestModel codeRequestModel) throws Exception {
        return ResponseEntity.ok(ojService.executeCode(codeRequestModel));
    }
}
