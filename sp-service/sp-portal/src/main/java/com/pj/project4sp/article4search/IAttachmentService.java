package com.pj.project4sp.article4search;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface IAttachmentService {

    void indexFile(Long articleId, List<String> urls, Long userId, LocalDateTime time);

    void deleteFile(List<String> urls);

    List<AttachmentVo> search(String key);

}
