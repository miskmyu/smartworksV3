package net.smartworks.server.engine.resource.dao;

import java.util.List;
import java.util.Map;

import net.smartworks.server.engine.resource.exception.SmartServerRuntimeException;
import net.smartworks.server.engine.resource.model.IFormDef;
import net.smartworks.server.engine.resource.model.IFormFieldDef;
import net.smartworks.server.engine.resource.model.IFormModel;
import net.smartworks.server.engine.resource.model.IFormModelList;
import net.smartworks.server.engine.resource.model.IPackageModel;
import net.smartworks.server.engine.resource.model.IPackageModelList;
import net.smartworks.server.engine.resource.model.IProcessModel;
import net.smartworks.server.engine.resource.model.IProcessModelList;
import net.smartworks.server.engine.resource.model.IWorkTypeModel;

/**
 * 프로세스, 업무 폼, 워크 타입 등의 프로세스를 모델링하기 위한 리소스들을 관리하기 위한 DAO
 * 
 * @author jhnam
 * @version $Id: IResourceDesigntimeDao.java,v 1.1 2011/11/08 03:15:15 kmyu Exp $
 */
public interface IResourceDesigntimeDao {

	/**
	 * 패키지 아이디로 최신 버전을 찾는다.
	 * 
	 * @param packageId
	 * @return
	 * @throws SmartServerRuntimeException
	 */
	public int retrieveLatestPackageVersion(String packageId) throws SmartServerRuntimeException;
	
	/**
	 * 프로세스 아이디로 최신 버전을 찾는다.
	 * 
	 * @param processId
	 * @return
	 * @throws SmartServerRuntimeException
	 */
	public int retrieveLatestProcessVersion(String processId) throws SmartServerRuntimeException;
	
	/**
	 * 폼 아이디로 최신 버전을 찾는다.
	 * 
	 * @param formId
	 * @return
	 * @throws SmartServerRuntimeException
	 */
	public int retrieveLatestFormVersion(String formId) throws SmartServerRuntimeException;	
	
	/**
	 * 패키지 아이디로 패키지를 찾는다.
	 * 
	 * @param packageId
	 * @return
	 * @throws SmartServerRuntimeException
	 */
	public IPackageModel retrieveLatestPackage(String packageId) throws SmartServerRuntimeException;
	
	/**
	 * 패키지 아이디, 패키지 버전으로 패키지를 찾는다.
	 * 
	 * @param packageId
	 * @param version
	 * @return
	 * @throws SmartServerRuntimeException
	 */
	public IPackageModel retrievePackage(String packageId, int version) throws SmartServerRuntimeException;
	
	/**
	 * 프로세스 유니크 아이디로 프로세스를 찾는다.
	 * 
	 * @param processUid
	 * @return
	 * @throws SmartServerRuntimeException
	 */
	public IProcessModel retrieveProcess(String processUid) throws SmartServerRuntimeException;
	
	/**
	 * 프로세스 아이디, 프로세스 버전으로 프로세스를 찾는다.
	 * 
	 * @param processId
	 * @param version
	 * @return
	 * @throws SmartServerRuntimeException
	 */
	public IProcessModel retrieveProcess(String processId, int version) throws SmartServerRuntimeException;
	
	/**
	 * 패키지 아이디, 버전으로 프로세스를 찾는다.
	 * 
	 * @param packageId
	 * @param version
	 * @return
	 * @throws SmartServerRuntimeException
	 */
	public IProcessModel retrieveProcessByPackage(String packageId, int version) throws SmartServerRuntimeException;
	
	/**
	 * 프로세스 아이디로 최신 버전의 프로세스를 찾는다.
	 * 
	 * @param processId
	 * @return
	 * @throws SmartServerRuntimeException
	 */
	public IProcessModel retrieveLatestProcess(String processId) throws SmartServerRuntimeException;
	
	/**
	 * 패키지 아이디로 부터 최신 버전의 프로세스를 찾는다.
	 * 
	 * @param packageId
	 * @return
	 * @throws SmartServerRuntimeException
	 */
	public IProcessModel retrieveLatestProcessByPackage(String packageId) throws SmartServerRuntimeException;
	
	/**
	 * 프로세스 아이디로 마지막 버전의 프로세스 내용을 찾는다.
	 * 
	 * @param processId
	 * @return
	 * @throws SmartServerRuntimeException
	 */
	public String retrieveLatestProcessContent(String processId) throws SmartServerRuntimeException;
	
	/**
	 * 프로세스 유니크 아이디로 프로세스 내용을 찾는다.
	 * 
	 * @param processUid
	 * @return
	 * @throws SmartServerRuntimeException
	 */
	public String retrieveProcessContent(String processUid) throws SmartServerRuntimeException;
	
	/**
	 * 프로세스 아이디, 버전으로 프로세스 내용을 찾는다.
	 * 
	 * @param processId
	 * @param version
	 * @return
	 * @throws SmartServerRuntimeException
	 */
	public String retrieveProcessContent(String processId, int version) throws SmartServerRuntimeException;	
		
	/**
	 * 업무 폼 유니크 아이디로 업무 폼을 찾는다.
	 * 
	 * @param formUid
	 * @return
	 * @throws SmartServerRuntimeException
	 */
	public IFormModel retrieveForm(String formUid) throws SmartServerRuntimeException;
	
	/**
	 * 업무 폼 아이디, 버전으로 업무 폼을 찾는다.
	 * 
	 * @param formId
	 * @param version
	 * @return
	 * @throws SmartServerRuntimeException
	 */
	public IFormModel retrieveForm(String formId, int version) throws SmartServerRuntimeException;
	
	/**
	 * 업무 폼 아이디로 최신 버전의 업무 폼을 찾는다.
	 * 
	 * @param formId
	 * @return
	 * @throws SmartServerRuntimeException
	 */
	public IFormModel retrieveLatestForm(String formId) throws SmartServerRuntimeException;
	
	/**
	 * 폼 아이디로 마지막 버전의 폼 내용을 찾는다.
	 * 
	 * @param formId
	 * @return
	 * @throws SmartServerRuntimeException
	 */
	public String retrieveLatestFormContent(String formId) throws SmartServerRuntimeException;
	
	/**
	 * 폼 유니크 아이디로 폼 내용을 찾는다.
	 * 
	 * @param formUid
	 * @return
	 * @throws SmartServerRuntimeException
	 */
	public String retrieveFormContent(String formUid) throws SmartServerRuntimeException;
	
	/**
	 * 폼 아이디, 버전로 폼 내용을 찾는다.
	 * 
	 * @param formId
	 * @param version
	 * @return
	 * @throws SmartServerRuntimeException
	 */
	public String retrieveFormContent(String formId, int version) throws SmartServerRuntimeException;
	
	/**
	 * 워크타입 아이디로 부터 워크 타입을 찾는다.
	 * 
	 * @param workTypeId
	 * @return
	 * @throws SmartServerRuntimeException
	 */
	public IWorkTypeModel retrieveWorkType(String workTypeId) throws SmartServerRuntimeException;
	
	/**
	 * 폼 유니크 아이디로 부터 워크 타입을 찾는다.
	 * 
	 * @param formUid
	 * @return
	 * @throws SmartServerRuntimeException
	 */
	public IWorkTypeModel retrieveWorkTypeByForm(String formUid) throws SmartServerRuntimeException;
	
	/**
	 * 폼 아이디, 버전으로 워크 타입을 찾는다.
	 * 
	 * @param formId
	 * @param version
	 * @return
	 * @throws SmartServerRuntimeException
	 */
	public IWorkTypeModel retrieveWorkTypeByForm(String formId, int version) throws SmartServerRuntimeException;
	
	/**
	 * 폼 아이디로 모든 버전의 폼 리스트를 찾는다.
	 * 
	 * @param formId
	 * @return
	 * @throws SmartServerRuntimeException
	 */
	public List<Integer> findFormVersions(String formId) throws SmartServerRuntimeException;
	
	/**
	 * 프로세스 아이디로 모든 버전의 프로세스를 찾는다.
	 * 
	 * @param processId
	 * @return
	 * @throws SmartServerRuntimeException
	 */
	public List<Integer> findProcessVersions(String processId) throws SmartServerRuntimeException;
	
	/**
	 * 프로세스 아이디로 모든 버전의 프로세스를 찾는다.
	 * 
	 * @param processId
	 * @return
	 * @throws SmartServerRuntimeException
	 */
	public List<IProcessModel> findProcessVersionList(String processId) throws SmartServerRuntimeException;
	
	/**
	 * 프로세스 아이디와 프로세스 버전으로 업무 폼 리스트를 찾는다.
	 * 
	 * @param processId
	 * @param version
	 * @return
	 * @throws SmartServerRuntimeException
	 */
	public List<IFormModel> findFormByProcess(String processId, int version) throws SmartServerRuntimeException;
	
	/**
	 * 프로세스 유니크 아이디로 해당 프로세스에 소속된 업무 폼 리스트를 찾는다.
	 * 
	 * @param processUid
	 * @return
	 * @throws SmartServerRuntimeException
	 */
	public List<IFormModel> findFormByProcess(String processUid) throws SmartServerRuntimeException;
	
	/**
	 * 프로세스 아이디로 프로세스에 소속된 최신 버전의 업무 폼 리스트를 찾는다.
	 * 
	 * @param processId
	 * @return
	 * @throws SmartServerRuntimeException
	 */
	public List<IFormModel> findLatestFormByProcess(String processId) throws SmartServerRuntimeException;
	
	/**
	 * 패키지 아이디, 버전으로 해당 패키지에 소속된 모든 폼 리스트를 찾는다.
	 * 
	 * @param packageId
	 * @param version
	 * @return
	 * @throws SmartServerRuntimeException
	 */
	public List<IFormModel> findFormByPackage(String packageId, int version) throws SmartServerRuntimeException;

	/**
	 * 폼 아이디로 폼 필드 리스트를 찾는다.
	 * 
	 * @param formId
	 * @param deployedCondition 배포된 검색 조건만 원할 경우 true
	 * @return
	 * @throws SmartServerRuntimeException
	 */
	public List<IFormFieldDef> findFormFieldByForm(String formId, boolean deployedCondition) throws SmartServerRuntimeException;
	
	/**
	 * 패키지 아이디, 버전으로 패키지에 소속된 단위업무 리스트를 찾는다.
	 * 
	 * @param packageId
	 * @param version
	 * @return
	 * @throws SmartServerRuntimeException
	 */
	public List<IFormModel> findSingleFormByPackage(String packageId, int version) throws SmartServerRuntimeException;
	
	/**
	 * 패키지 아이디로 패키지에 소속된 최신 버전의 업무 폼 리스트를 찾는다.
	 * 
	 * @param packageId
	 * @return
	 * @throws SmartServerRuntimeException
	 */
	public List<IFormModel> findLatestFormByPackage(String packageId) throws SmartServerRuntimeException;
	
	/**
	 * 패키지 아이디, 버전으로 모든 폼을 찾아 폼에 속한 필드를 찾는다. 
	 * 
	 * @param packageId
	 * @param version
	 * @return
	 * @throws SmartServerRuntimeException
	 */
	public List<IFormDef> findFormFieldByPackage(String packageId, int version) throws SmartServerRuntimeException;
	
	/**
	 * 패키지 아이디, 버전으로 소속 패키지의 워크 타입 리스트를 찾는다.
	 * 
	 * @param packageId
	 * @param version
	 * @return
	 * @throws SmartServerRuntimeException
	 */
	public List<IWorkTypeModel> findWorkType(String packageId, int version) throws SmartServerRuntimeException;
	
	/**
	 * 검색 조건에 맞는 패키지 개수
	 *  
	 * @param condition
	 * @return
	 * @throws SmartServerRuntimeException
	 */
	public int searchPackageCount(Map<String, Object> condition) throws SmartServerRuntimeException;
	
	/**
	 * 검색 조건에 맞는 프로세스 개수
	 *  
	 * @param condition
	 * @return
	 * @throws SmartServerRuntimeException
	 */
	public int searchProcessCount(Map<String, Object> condition) throws SmartServerRuntimeException;
	
	/**
	 * 검색 조건에 맞는 업무 폼 개수
	 *  
	 * @param condition
	 * @return
	 * @throws SmartServerRuntimeException
	 */
	public int searchFormCount(Map<String, Object> condition) throws SmartServerRuntimeException;
	
	/**
	 * 카테고리에 소속된 패키지들의 최신 버전을 검색
	 * 
	 * @param categoryId
	 * @return
	 * @throws SmartServerRuntimeException
	 */
	public IPackageModelList searchLatestPackageList(String categoryId) throws SmartServerRuntimeException;
	
	/**
	 * 검색 조건에 따라서 업무패키지 조회
	 * 
	 * @param condition
	 * @param orderCondition
	 * @param pageCount
	 * @param recordCount
	 * @return
	 * @throws SmartServerRuntimeException
	 */
	public IPackageModelList searchPackageByCondition(Map<String, Object> condition, Map<String, String> orderCondition, int pageCount, int recordCount) throws SmartServerRuntimeException;	
	
	/**
	 * 검색 조건에 따라서 프로세스 조회
	 * 
	 * @param condition
	 * @param pageCount
	 * @param recordCount
	 * @return
	 * @throws SmartServerRuntimeException
	 */
	public IProcessModelList searchProcessByCondition(Map<String, Object> condition, int pageCount, int recordCount) throws SmartServerRuntimeException;

	/**
	 * 검색 조건에 따라서 업무 폼 조회
	 * 
	 * @param condition
	 * @param pageCount
	 * @param recordCount
	 * @return
	 * @throws SmartServerRuntimeException
	 */
	public IFormModelList searchFormByCondition(Map<String, Object> condition, int pageCount, int recordCount) throws SmartServerRuntimeException;	
	
	/**
	 * 카테고리 하위에 패키지를 생성합니다.
	 * 
	 * @param userId
	 * @param categoryId
	 * @param pkg
	 * @throws SmartServerRuntimeException
	 */
	public IPackageModel createPackage(String userId, String categoryId, IPackageModel pkg) throws SmartServerRuntimeException;
	
	/**
	 * 카테고리 하위에 패키지 이름이 name인 패키지를 생성한다. 그리고 타입에 따른 프로세스 혹은 화면을 생성한다.
	 * 
	 * @param userId
	 * @param categoryId
	 * @param type
	 * @param name
	 * @param desc
	 * @return
	 * @throws SmartServerRuntimeException
	 */
	public IPackageModel createPackage(String userId, String categoryId, String type, String name, String desc) throws SmartServerRuntimeException;
			
	/**
	 * 카테고리 하위에 패키지를 이름이 name인 것으로 생성한다.
	 * 
	 * @param userId
	 * @param categoryId
	 * @param name
	 * @param desc
	 * @return
	 * @throws SmartServerRuntimeException
	 */
	public IPackageModel createPackage(String userId, String categoryId, String name, String desc) throws SmartServerRuntimeException;
	
	/**
	 * 프로세스를 새로 생성한다.
	 * 
	 * @param userId
	 * @param packageId
	 * @param version
	 * @param name
	 * @return
	 * @throws SmartServerRuntimeException
	 */
	public IProcessModel createProcess(String userId, String packageId, int version, String name) throws SmartServerRuntimeException;

	/**
	 * 폼을 새로 생성한다.
	 * 
	 * @param userId
	 * @param packageId
	 * @param version
	 * @param type
	 * @param name
	 * @return
	 * @throws SmartServerRuntimeException
	 */
	public IFormModel createForm(String userId, String packageId, int version, String type, String name) throws SmartServerRuntimeException;
	/**
	 * 폼을 새로 생성한다.
	 * 
	 * @param userId
	 * @param obj
	 * @return
	 * @throws SmartServerRuntimeException
	 */
	public IFormModel createForm(String userId, IFormModel obj) throws SmartServerRuntimeException;
	/**
	 * 폼을 복사하여 붙입니다(복사되어질 폼의 이름을 입력 받아 그이름으로 생성합니다.).
	 * 
	 * @param userId
	 * @param formId
	 * @param version
	 * @param toPkgId
	 * @param toPkgVer
	 * @param newFormName
	 * @return
	 * @throws SmartServerRuntimeException
	 */
	public IFormModel cloneForm(String userId, String formId, int version, String toPkgId, int toPkgVer, String newFormName) throws SmartServerRuntimeException;
	/**
	 * 폼을 복사하여 붙입니다.
	 * 
	 * @param userId
	 * @param formId
	 * @param version
	 * @param toPkgId
	 * @param toPkgVer
	 * @return
	 * @throws SmartServerRuntimeException
	 */
	public IFormModel cloneForm(String userId, String formId, int version, String toPkgId, int toPkgVer) throws SmartServerRuntimeException;
	
	/**
	 * 패키지를 복사하여 붙입니다.
	 * 
	 * @param userId
	 * @param categoryId
	 * @param packageId
	 * @param version
	 * @return
	 * @throws SmartServerRuntimeException
	 */
	public IPackageModel clonePackage(String userId, String categoryId, String packageId, int version) throws SmartServerRuntimeException;

	/**
	 * 패키지를 복사하여 붙입니다.
	 * 
	 * @param userId
	 * @param categoryId
	 * @param packageId
	 * @param version
	 * @return
	 * @throws SmartServerRuntimeException
	 */
	public IPackageModel clonePackage(String userId, String categoryId, String targetPackageName, String targetPackageDesc, String packageId, int version) throws SmartServerRuntimeException;
	
	/**
	 * 업무패키지를 업데이트한다.
	 * 
	 * @param userId
	 * @param packageModel
	 * @return
	 * @throws SmartServerRuntimeException
	 */
	public IPackageModel updatePackage(String userId, IPackageModel packageModel) throws SmartServerRuntimeException;
	
	/**
	 * 프로세스 메타 정보를 업데이트한다.
	 * 
	 * @param userId
	 * @param processModel
	 * @return
	 * @throws SmartServerRuntimeException
	 */
	public IProcessModel updateProcess(String userId, IProcessModel processModel) throws SmartServerRuntimeException;
	
	/**
	 * 업무 폼 이름을 변경한다.
	 * 
	 * @param userId
	 * @param formId
	 * @param version
	 * @param formName
	 * @throws SmartServerRuntimeException
	 */
	public void updateFormName(String userId, String formId, int version, String formName) throws SmartServerRuntimeException;
	
	/**
	 * 프로세스 이름을 변경한다.
	 * 
	 * @param processId
	 * @param version
	 * @param processName
	 * @throws SmartServerRuntimeException
	 */
	public void updateProcessName(String processId, int version, String processName) throws SmartServerRuntimeException;
	
	/**
	 * 프로세스 내용을 업데이트한다.
	 * 
	 * @param userId
	 * @param processUid
	 * @param xmlContent
	 * @return
	 * @throws SmartServerRuntimeException
	 */
	public void updateProcessContent(String userId, String processUid, String xmlContent) throws SmartServerRuntimeException;
	
	/**
	 * 프로세스 내용을 업데이트한다.
	 * 
	 * @param userId
	 * @param processId
	 * @param version
	 * @param xmlContent
	 * @return
	 * @throws SmartServerRuntimeException
	 */
	public void updateProcessContent(String userId, String processId, int version, String xmlContent) throws SmartServerRuntimeException;
	
	/**
	 * 최신 버전의 프로세스 내용을 업데이트한다.
	 * 
	 * @param userId
	 * @param processId
	 * @param xmlContent
	 * @throws SmartServerRuntimeException
	 */
	public void updateLatestProcessContent(String userId, String processId, String xmlContent) throws SmartServerRuntimeException;
	
	/**
	 * 업무 폼 메타 정보를 업데이트한다.
	 * 
	 * @param userId
	 * @param formModel
	 * @return
	 * @throws SmartServerRuntimeException
	 */
	public IFormModel updateForm(String userId, IFormModel formModel) throws SmartServerRuntimeException;
	
	/**
	 * 업무 폼의 내용을 업데이트한다.
	 * 
	 * @param userId
	 * @param formUid
	 * @param xmlContent
	 * @throws SmartServerRuntimeException
	 */
	public void updateFormContent(String userId, String formUid, String xmlContent) throws SmartServerRuntimeException;
	
	/**
	 * 업무 폼의 내용을 업데이트한다.
	 * 
	 * @param userId
	 * @param formId
	 * @param version
	 * @param xmlContent
	 * @throws SmartServerRuntimeException
	 */
	public void updateFormContent(String userId, String formId, int version, String xmlContent) throws SmartServerRuntimeException;
	
	/**
	 * 최신 버전의 업무 폼의 내용을 업데이트한다.
	 *  
	 * @param userId
	 * @param formId
	 * @param xmlContent
	 * @throws SmartServerRuntimeException
	 */
	public void updateLatestFormContent(String userId, String formId, String xmlContent) throws SmartServerRuntimeException;
	
	/**
	 * 워크 타입 정보를 업데이트한다.
	 * 
	 * @param userId
	 * @param workType
	 * @throws SmartServerRuntimeException
	 */
	public IWorkTypeModel updateWorkType(String userId, IWorkTypeModel workType) throws SmartServerRuntimeException;
	
	/**
	 * 패키지 아이디로 모든 버전의 패키지를 삭제한다. 관련 프로세스, 업무 폼등도 모두 삭제한다.
	 * 
	 * @param userId
	 * @param packageId
	 * @throws SmartServerRuntimeException
	 */
	public void deletePackageAll(String userId, String packageId) throws SmartServerRuntimeException;
	
	/**
	 * 패키지 아이디 버전으로 해당 패키지를 삭제한다. 관련 프로세스, 업무 폼등도 모두 삭제한다.
	 * 
	 * @param userId
	 * @param packageId
	 * @param version
	 * @throws SmartServerRuntimeException
	 */
	public void deletePackage(String userId, String packageId, int version) throws SmartServerRuntimeException;	
		
	/**
	 * 프로세스 유니크 아이디로 프로세스를 삭제한다.
	 * 
	 * @param userId
	 * @param processUid
	 * @throws SmartServerRuntimeException
	 */
	public void deleteProcess(String userId, String processUid) throws SmartServerRuntimeException;
	
	/**
	 * 프로세스 아이디, 버전으로 프로세스를 찾아 프로세스를 삭제한다.
	 * 
	 * @param uesrId
	 * @param processId
	 * @param version
	 * @throws SmartServerRuntimeException
	 */
	public void deleteProcess(String uesrId, String processId, int version) throws SmartServerRuntimeException;
	
	/**
	 * 폼 유니크 아이디로 폼을 찾아 삭제한다.
	 * 
	 * @param userId
	 * @param formUid
	 * @throws SmartServerRuntimeException
	 */
	public void deleteForm(String userId, String formUid) throws SmartServerRuntimeException;
	
	/**
	 * 업무 폼 아이디, 버전으로 폼을 찾아 삭제한다.
	 * 
	 * @param userId
	 * @param formId
	 * @param version
	 * @throws SmartServerRuntimeException
	 */
	public void deleteForm(String userId, String formId, int version) throws SmartServerRuntimeException;
	
	/**
	 * 패키지 체크인
	 * 
	 * @param userId
	 * @param packageId
	 * @param version
	 * @throws SmartServerRuntimeException
	 */
	public void checkInPackage(String userId, String packageId, int version) throws SmartServerRuntimeException;
	
	/**
	 * 패키지 체크아웃
	 * 
	 * @param userId
	 * @param packageId
	 * @param version
	 * @throws SmartServerRuntimeException
	 */
	public void checkOutPackage(String userId, String packageId, int version) throws SmartServerRuntimeException;
	
	/**
	 * 프로세스 체크인
	 * 
	 * @param userId
	 * @param processId
	 * @param version
	 * @throws SmartServerRuntimeException
	 */
	public void checkInProcess(String userId, String processId, int version) throws SmartServerRuntimeException;
	
	/**
	 * 프로세스 체크아웃
	 * 
	 * @param userId
	 * @param processId
	 * @param version
	 * @return 프로세스 내용 (XML)
	 * @throws SmartServerRuntimeException
	 */
	public String checkOutProcess(String userId, String processId, int version) throws SmartServerRuntimeException;
	
	/**
	 * 폼을 체크인한다.
	 * 
	 * @param userId
	 * @param formId
	 * @param version
	 * @throws SmartServerRuntimeException
	 */
	public void checkInForm(String userId, String formId, int version) throws SmartServerRuntimeException;
	
	/**
	 * 폼을 체크아웃한다.
	 * 
	 * @param userId
	 * @param formId
	 * @param version
	 * @return 폼 내용 (XML)
	 * @throws SmartServerRuntimeException
	 */
	public String checkOutForm(String userId, String formId, int version) throws SmartServerRuntimeException;
	
	/**
	 * 패키지를 배치한다. - 패키지에 속한 프로세스, 폼을 모두 배치한다.
	 * 
	 * @param userId
	 * @param packageId
	 * @param version
	 * @throws SmartServerRuntimeException
	 */
	public void deployPackage(String userId, String companyId, String packageId, int version) throws SmartServerRuntimeException;
	
	/**
	 * 패키지를 배치해제한다. - 패키지에 속한 프로세스, 폼을 모두 배치해제한다.
	 * 
	 * @param userId
	 * @param packageId
	 * @param version
	 * @throws SmartServerRuntimeException
	 */
	public void undeployPackage(String userId, String packageId, int version) throws SmartServerRuntimeException;
	
//	/**
//	 * 폼 매핑 모델 생성
//	 * 
//	 * @param formMapping
//	 * @return
//	 * @throws SmartServerRuntimeException
//	 */
//	public IFormMappingModel createFormMapping(IFormMappingModel formMapping) throws SmartServerRuntimeException;
//	
//	/**
//	 * 폼 아이디, 폼 버전으로 모든 폼 매핑을 삭제한다.
//	 * 
//	 * @param formId
//	 * @param formVersion
//	 * @throws SmartServerRuntimeException
//	 */
//	public void deleteFormMapping(String formId, int formVersion) throws SmartServerRuntimeException;
//	
//	/**
//	 * 폼 매핑 모델 업데이트
//	 * 
//	 * @param mapping
//	 * @throws SmartServerRuntimeException
//	 */
//	public void updateFormMapping(IFormMappingModel mapping) throws SmartServerRuntimeException;
//		
//	/**
//	 * formId, formVersion, mappingNo로 부터 폼 매핑 정보를 추출한다.
//	 * 
//	 * @param formId
//	 * @param formVersion
//	 * @param mappingNo
//	 * @return
//	 * @throws SmartServerRuntimeException
//	 */
//	public IFormMappingModel retrieveFormMapping(String formId, int formVersion, String mappingNo) throws SmartServerRuntimeException;
//	
//	/**
//	 * 폼 아이디, 폼 버전, 매핑 번호로 폼 매핑을 조회한다.
//	 * 
//	 * @param formId
//	 * @param formVersion
//	 * @return
//	 * @throws SmartServerRuntimeException
//	 */
//	public List<IFormMappingModel> findFormMapping(String formId, int formVersion) throws SmartServerRuntimeException;
	
	/**
	 * categoryId로 카테고리에 소속된 배치된 최신 버전의 프로세스를 검색
	 * 
	 * @param categoryId
	 * @return
	 * @throws SmartServerRuntimeException
	 */
	public List<IProcessModel> findLatestProcessByCategory(String categoryId) throws SmartServerRuntimeException;
	
	/**
	 * categoryId로 카테고리에 소속된 모든 폼 리스트를 검색 
	 * 
	 * @param categoryId
	 * @param type
	 * @return
	 * @throws SmartServerRuntimeException
	 */
	public List<IFormModel> findLatestFormByCategory(String categoryId, String type) throws SmartServerRuntimeException;
		
}
