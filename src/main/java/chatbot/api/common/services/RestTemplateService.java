package chatbot.api.common.services;

import chatbot.api.common.domain.ResponseDTO;
import chatbot.api.skillhub.domain.ResultAboutReservationDelete;
import chatbot.api.textbox.domain.Build;
import chatbot.api.textbox.domain.path.HrdwrDTO;
import chatbot.api.textbox.domain.reservation.ReservationDTO;
import chatbot.api.textbox.domain.reservation.ReservationListDTO;
import chatbot.api.textbox.domain.response.ResponseHrdwrInfo;
import chatbot.api.textbox.domain.transfer.CmdReservationDeletion;
import chatbot.api.textbox.repository.BuildRepository;
import chatbot.api.textbox.repository.ReservationListRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.util.ArrayList;

import static chatbot.api.textbox.utils.TextBoxConstants.BUTTON_TYPE_RESERVATION_DELETION;

@Service
@AllArgsConstructor
@Slf4j
public class RestTemplateService {

    private RestTemplate restTemplate;

    private BuildRepository buildRepository;

    private ReservationListRepository reservationListRepository;


    public ResponseHrdwrInfo requestHrdwrsInfo(String providerId) {
        log.info("=========== RestTemplate -> requestHrdwrsInfo 시작 ===========");
        Build reBuild = buildRepository.find(providerId);
        String url = "http://" + reBuild.getPath().getExternalIp() + ":" + reBuild.getPath().getExternalPort() + "/dev";
        log.info("하드웨어 목록 조회 URL -> " + url);
        log.info("=========== RestTemplate -> requestHrdwrsInfo 종료 ===========");

        return restTemplate.getForObject(url, ResponseHrdwrInfo.class);

        // 일단 데이터 받은걸로 가정합시다.
        /*
        HrdwrDTO[] hrdwrs = new HrdwrDTO[2];
        hrdwrs[0] = HrdwrDTO.builder()
                .hrdwrMac("12:12:12:12:12:12")
                .authKey("b776d155a4d44816969408f831600e2d")
                .build();
        hrdwrs[1] = HrdwrDTO.builder()
                .hrdwrMac("72:72:72:72:72:72")
                .authKey("aaaabbbbccccddddeeeeffffgggghhhh")
                .build();

        ResponseHrdwrInfo hrdwrInfo = ResponseHrdwrInfo.builder()
                .hrdwrsInfo(hrdwrs)
                .status(true)
                .build();
        log.info("INFO >> DEV INFO 확인 : " + hrdwrInfo.toString());
        return hrdwrInfo;
        */
    }


    public ReservationListDTO requestReservationList(String providerId) {
        log.info("=========== RestTemplate -> requestReservationList 시작 ===========");
        Build reBuild = buildRepository.find(providerId);
        String url = "http://" + reBuild.getPath().getExternalIp() + ":" + reBuild.getPath().getExternalPort() + "/hub/" + reBuild.getPath().getHrdwrMacAddr() + "/reservation";
        log.info("예약 목록 조회 URL -> " + url);
        log.info("=========== RestTemplate -> requestReservationList 종료 ===========");
        // 나중에 주석 풀기
        ReservationListDTO reservationList = restTemplate.getForObject(url, ReservationListDTO.class);
        log.info("ReservationList 확인 -> " + reservationList.getReserveList());
        return reservationList;
/*
        ReservationListDTO reservationListDTO = new ReservationListDTO();
        reservationListDTO.setReserveList(new ArrayList<ReservationDTO>());
        ReservationDTO reservationDTO_1 = ReservationDTO.builder()
                .reservationId(1)
                .eventCode(2)
                .actionAt(new Timestamp(157240000000L))
                .build();
        ReservationDTO reservationDTO_2 = ReservationDTO.builder()
                .reservationId(2)
                .eventCode(3)
                .actionAt(new Timestamp(157240005555L))
                .build();
        reservationListDTO.getReserveList().add(reservationDTO_1);
        reservationListDTO.getReserveList().add(reservationDTO_2);
        return reservationListDTO;
        */
    }


    public String requestForReservationDeletion(String providerId, Integer resIdForDeletion) {
        log.info("=========== RestTemplate -> request ReservationIdForDeletion 시작 ===========");
        // redis 에서 데이터 가져오기, 및 제거
        ReservationListDTO reservationList = reservationListRepository.find(providerId);
        reservationListRepository.delete(providerId);

        // 전송할 명령 만들기
        CmdReservationDeletion cmdReservationDeletion = CmdReservationDeletion.builder()
                .btnType(BUTTON_TYPE_RESERVATION_DELETION)
                .additional(resIdForDeletion)
                .eventCode(null)
                .build();
        for(ReservationDTO tempReservation : reservationList.getReserveList()) {
            if(resIdForDeletion == tempReservation.getReservationId()) {
                cmdReservationDeletion.setEventCode(tempReservation.getEventCode());
                break;
            }
        }

        //  url 완성
        String url = reservationList.getUrl() + resIdForDeletion;

        log.info("예약 취소 URL -> " + url);
        ResultAboutReservationDelete resultAboutReservationDelete = restTemplate.postForObject(url, null, ResultAboutReservationDelete.class);

//        ResponseDTO responseDTO = ResponseDTO.builder().msg("명령이 성공적으로 삭제되었습니다.").build();

        log.info("=========== RestTemplate -> requestReservationIdForDeletion 종료 ===========");
        return resultAboutReservationDelete.getMsg();
    }
}