package com.example.projectmanagement.Repository;

import com.example.projectmanagement.Model.ProjectMember;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {
    boolean existsByProjectIdAndUserId(Long projectId, Long userId);

    List<ProjectMember> findByProjectId(Long projectId);
@Transactional
    void deleteByProjectIdAndUserId(Long projectId, Long userId);

}

