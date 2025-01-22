package com.example.memo.controller;

import com.example.memo.dto.MemoRequestDto;
import com.example.memo.dto.MemoResponseDto;
import com.example.memo.entity.Memo;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/memos")
public class MemoController {
    private final Map<Long, Memo> memoList = new HashMap<>();

    @PostMapping
    public MemoResponseDto createMemo(
            @RequestBody MemoRequestDto requestDto
    ) {
        Long memoId = memoList.isEmpty() ? 1 : Collections.max(memoList.keySet())+1;

        Memo memo = new Memo(memoId, requestDto.getTitle(), requestDto.getContents());

        memoList.put(memoId, memo);
        return new MemoResponseDto(memo);
    }

    @GetMapping("/{id}")
    public MemoResponseDto findMemoById(
            @PathVariable  Long id
    ) {
        Memo memo = memoList.get(id);
        return new MemoResponseDto(memo);
    }

    @PutMapping("/{id}")
    public MemoResponseDto updateMemoById(
        @PathVariable Long id,
        @RequestBody MemoRequestDto requestDto
    ) {
        Memo memo = memoList.get(id);
        memo.update(requestDto);
        return new MemoResponseDto(memo);
    }

    @DeleteMapping("/{id}")
    public void deleteMemo(
            @PathVariable Long id
    ) {
        memoList.remove(id);
    }
}
