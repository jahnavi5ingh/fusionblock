package com.sumit.aistudio.backend.linker;

import com.sumit.aistudio.backend.linker.LinkInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LinkInfoRepository extends JpaRepository<LinkInfo, String> {
}
