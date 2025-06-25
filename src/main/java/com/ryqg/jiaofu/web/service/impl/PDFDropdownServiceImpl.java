package com.ryqg.jiaofu.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ryqg.jiaofu.common.constants.SecurityConstants;
import com.ryqg.jiaofu.common.converter.PDFDropdownConverter;
import com.ryqg.jiaofu.domain.dto.PDFDropdownDTO;
import com.ryqg.jiaofu.domain.pojo.PDFDropdown;
import com.ryqg.jiaofu.domain.vo.PDFDropdownVO;
import com.ryqg.jiaofu.web.mapper.PDFDropdownMapper;
import com.ryqg.jiaofu.web.service.PDFDropdownService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PDFDropdownServiceImpl implements PDFDropdownService {
    private final PDFDropdownMapper pdfDropdownMapper;

    private final PDFDropdownConverter pdfDropdownConverter;


    @Override
    public List<PDFDropdownVO> getDropdown() {
        List<PDFDropdown> list = pdfDropdownMapper.getDropdown();

        Set<String> ids = list.stream().map(PDFDropdown::getId).collect(Collectors.toSet());
        Set<String> parentIds = list.stream().map(PDFDropdown::getParentId).collect(Collectors.toSet());
        List<String> rootIds = parentIds.stream()
                .filter(id -> !ids.contains(id))
                .toList();

        // 使用递归函数来构建菜单树
        return rootIds.stream()
                .flatMap(rootId -> buildTree(rootId, list).stream())
                .collect(Collectors.toList());
    }

    @Override
    public void addDropdown(PDFDropdownDTO pdfDropdownDTO) {
        PDFDropdown entity = pdfDropdownConverter.toEntity(pdfDropdownDTO);
        entity.setTreePath(generateTreePath(pdfDropdownDTO.getParentId()));
        pdfDropdownMapper.insert(entity);
    }

    @Override
    public PDFDropdownVO getFormData(String id) {
        return pdfDropdownConverter.toVO(pdfDropdownMapper.selectById(id));
    }

    private String generateTreePath(String parentId) {
        if (SecurityConstants.ROOT_NODE_ID.equals(parentId)) {
            return parentId;
        } else {
            PDFDropdown parent = pdfDropdownMapper.selectById(parentId);
            return parent != null ? parent.getTreePath() + "," + parent.getId() : null;
        }
    }


    @Override
    public void updateDropdown(PDFDropdownDTO pdfDropdownDTO) {
        PDFDropdown entity = pdfDropdownConverter.toEntity(pdfDropdownDTO);
        entity.setTreePath(generateTreePath(pdfDropdownDTO.getParentId()));
        pdfDropdownMapper.updateById(pdfDropdownConverter.toEntity(pdfDropdownDTO));
    }

    @Override
    public void deleteDropdown(String id) {
        LambdaQueryWrapper<PDFDropdown> lambdaQueryWrapper = new LambdaQueryWrapper<PDFDropdown>().eq(PDFDropdown::getId, id).or()
                .apply("CONCAT (',',tree_path,',') LIKE CONCAT('%,',{0},',%')", id);
        pdfDropdownMapper.delete(lambdaQueryWrapper);
    }

    private List<PDFDropdownVO> buildTree(String parentId, List<PDFDropdown> list) {
        Map<String, PDFDropdownVO> menuMap = list.stream()
                .map(pdfDropdownConverter::toVO)
                .peek(menuVO -> menuVO.setChildren(new ArrayList<>())) // 预先初始化 children 列表
                .collect(Collectors.toMap(PDFDropdownVO::getId, v -> v,(existing, replacement) -> existing, // 处理键冲突的合并函数
                        LinkedHashMap::new ));// 指定有序 Map 实现

        List<PDFDropdownVO> pdfDropdownVOS = new ArrayList<>();
        for (PDFDropdownVO pdfDropdownVO : menuMap.values()) {
            if (pdfDropdownVO.getParentId().equals(parentId)) {
                pdfDropdownVOS.add(pdfDropdownVO);
            } else {
                PDFDropdownVO parent = menuMap.get(pdfDropdownVO.getParentId());
                if (parent != null) {
                    parent.getChildren().add(pdfDropdownVO);
                }
            }
        }
        return pdfDropdownVOS;
    }
}
