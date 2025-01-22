package com.example.memo.controller;

import com.example.memo.dto.MemoRequestDto;
import com.example.memo.dto.MemoResponseDto;
import com.example.memo.entity.Memo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/memos")
public class MemoController {
    private final Map<Long, Memo> memoList = new HashMap<>();

    @PostMapping
    public ResponseEntity<MemoResponseDto> createMemo(
            @RequestBody MemoRequestDto requestDto
    ) {
        Long memoId = memoList.isEmpty() ? 1 : Collections.max(memoList.keySet())+1;

        Memo memo = new Memo(memoId, requestDto.getTitle(), requestDto.getContents());

        memoList.put(memoId, memo);
        return new ResponseEntity<>(new MemoResponseDto(memo), HttpStatus.CREATED);
    }

    @GetMapping
    public List<MemoResponseDto> findAll() {
        return memoList.values().stream().map(MemoResponseDto::new).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemoResponseDto> findMemoById(
            @PathVariable  Long id
    ) {
        if(!memoList.containsKey(id)) {
            // exception 처리
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Memo memo = memoList.get(id);
        return new ResponseEntity<MemoResponseDto>(new MemoResponseDto(memo), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MemoResponseDto> updateMemo(
        @PathVariable Long id,
        @RequestBody MemoRequestDto requestDto
    ) {
        if(!memoList.containsKey(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if(requestDto.getTitle()==null || requestDto.getContents()==null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Memo memo = memoList.get(id);
        memo.update(requestDto);
        return new ResponseEntity<MemoResponseDto>(new MemoResponseDto(memo), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MemoResponseDto> updateMemoTitle(
            @PathVariable Long id,
            @RequestBody MemoRequestDto requestDto
    ) {
        if(!memoList.containsKey(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if(requestDto.getTitle()==null || requestDto.getContents()==null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Memo memo = memoList.get(id);
        memo.updateTitle(requestDto);
        return new ResponseEntity<MemoResponseDto>(new MemoResponseDto(memo), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMemo(
            @PathVariable Long id
    ) {
        if(!memoList.containsKey(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        memoList.remove(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
