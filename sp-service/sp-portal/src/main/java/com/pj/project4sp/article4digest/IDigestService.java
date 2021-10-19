package com.pj.project4sp.article4digest;

import java.time.LocalDate;
import java.util.List;

public interface IDigestService {

    void collectDigest(LocalDate localDate);

    List<DigestIntroVo> getDigest(long userId);

    void sendEmail(String lastWeekId);
}
