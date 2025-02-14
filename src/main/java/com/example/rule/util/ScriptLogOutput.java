package com.example.rule.util;

import com.yomahub.liteflow.script.annotation.ScriptBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@ScriptBean
@Slf4j
public class ScriptLogOutput {
    //脚本日志输出
    public void print(String message) {
        log.info(message);
    }
}
