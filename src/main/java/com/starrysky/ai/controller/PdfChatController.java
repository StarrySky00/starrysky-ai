package com.starrysky.ai.controller;

import com.starrysky.ai.enums.TypeEnum;
import com.starrysky.ai.repository.IFileService;
import com.starrysky.ai.repository.InMemoryChatHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;


/**
 * @author StarrySky
 */
@RestController
@RequestMapping("/ai/pdf")
@RequiredArgsConstructor
public class PdfChatController {
    private final ChatClient pdfChatClient;
    private final VectorStore vectorStore;
    private final InMemoryChatHistoryRepository chatHistoryRepository;
    private final IFileService fileService;
    @RequestMapping(value = "/chat",produces = "text/html;charset=UTF-8")
    public Flux<String> chat(String prompt,String chatId){

        //1. 获取文件
        Resource resource = fileService.getFile(chatId);
        if(!resource.exists()){
            throw new RuntimeException("未找到文件");
        }

        //2. 保存会话id
        chatHistoryRepository.save(TypeEnum.PDF.getType(),chatId);

        //3. 对话
        return pdfChatClient.prompt()
                .user(prompt)
                .advisors(a->a.param(ChatMemory.CONVERSATION_ID,chatId))
                .advisors(a->a.param(QuestionAnswerAdvisor.FILTER_EXPRESSION,
                        String.format("file_name == '%s' ",resource.getFilename())))
                .stream()
                .content();
    }
}
