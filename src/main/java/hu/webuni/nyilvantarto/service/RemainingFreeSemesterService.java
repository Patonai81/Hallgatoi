package hu.webuni.nyilvantarto.service;


import hu.webuni.nyilvantarto.wsclient.RemainingFreeSemesterIF;
import hu.webuni.nyilvantarto.wsclient.RemainingFreeSemesterServiceService;
import org.springframework.stereotype.Service;

@Service
public class RemainingFreeSemesterService {


    public int getRemainingSemester(int central_studentId){

       RemainingFreeSemesterIF remainingIF =  new RemainingFreeSemesterServiceService().getRemainingFreeSemesterServicePort();
      return  remainingIF.getRemainingSemester(2);
        /*
        if (new Random().nextInt(2) == 0)
            throw new BackendNotAvailableException();
*/
    }



}
