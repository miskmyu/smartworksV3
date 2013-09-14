-- 회사 : 단위 + 시스템
CREATE TABLE sworgcompany (
	id varchar(50) NOT NULL,
	name varchar(50),
	address varchar(4000),
	domainid varchar(50),
	workitemid varchar(50),
	creator	varchar(50),
	createdtime timestamp,
	modifier varchar(50),
	modifiedtime timestamp,
	primary key (id)
);	


-- 사용자 : 단위 + 시스템
CREATE TABLE sworguser (
	id varchar(50) NOT NULL,
	companyid varchar(50),
	deptid varchar(50),
	adjunctDeptIds varchar(500),
	roleid varchar(50),
	authid varchar(50),
	empno varchar(50),
	name varchar(255),
	nickName varchar(255),
	type varchar(50),
	pos varchar(50),
	email varchar(50),
	useMail Number(3),
	passwd varchar(50),
	lang varchar(20),
	stdtime varchar(20),
	picture varchar(50),
	sign varchar(50),
	useSign Number(3),
	domainid varchar(50),
	workitemid varchar(50),
	creator	varchar(50),
	createdtime timestamp,
	modifier	varchar(50),
	modifiedtime timestamp,
	retiree varchar(50),
	mobileNo varchar(50),
	internalNo varchar(50),
	locale varchar(20),
	timeZone varchar(20),
	hiredate timestamp,
	birthday timestamp,
	lunarbirthday int,
	homephoneno varchar(20),
	homeaddress varchar(200),
	primary key (id)
);

-- 부서 : 단위 + 시스템
CREATE TABLE sworgdept (
	id varchar(50) NOT NULL,
	companyid varchar(50),
	parentid varchar(50),
	type varchar(50),
	name varchar(50),
	description varchar(4000),
	domainid varchar(50),
	picture varchar(50),
	workitemid varchar(50),
	creator	varchar(50),
	createdtime timestamp,
	modifier	varchar(50),
	modifiedtime timestamp,
	primary key (id)
);

-- 역할 : 단위 + 시스템
CREATE TABLE sworgrole (
	id varchar(50) NOT NULL,
	companyid varchar(50),
	name varchar(50),
	description varchar(4000),
	domainid varchar(50),
	workitemid varchar(50),
	creator	varchar(50),
	createdtime timestamp,
	modifier	varchar(50),
	modifiedtime timestamp,
	primary key (id)
);

---- 일정 : 단위 + 시스템
--CREATE TABLE swschedule (
--	id 	varchar(50) NOT NULL,
--	userid varchar(50) NOT NULL,
--	title varchar(255),
--	startdate timestamp,
--	enddate timestamp,
--	type varchar(50),
--	place varchar(255),
--	fromtime timestamp,
--	totime timestamp,
--	referencer varchar(1000),
--	content varchar(4000),
--	alerttype varchar(50),
--	accesslevel varchar(20),
--	domainid varchar(50),
--	workitemid varchar(50),
--	creator	varchar(50),
--	createdtime timestamp,
--	modifier	varchar(50),
--	modifiedtime timestamp,
--	primary key (id)
--);

---- 부재 설정
--CREATE TABLE swabsentee (
--	id varchar(50) NOT NULL,
--	absentee varchar(30),
--	startdate timestamp,
--	enddate timestamp,
--	registeredtime timestamp,
--	entrustyn char(1),
--	entruster varchar(30),
--	reason varchar(4000),
--	primary key (id)
--);

---- 코드
--CREATE TABLE swcode (
--	id varchar(50) NOT NULL,
--	categoryid varchar(50),
--	code varchar(50),
--	name varchar(255),
--	disporder int,
--	description varchar(4000),
--	primary key (id)
--);

-- 리소스
CREATE TABLE swcategory (
	id varchar(50) NOT NULL,
	companyid varchar(100),
	parentid varchar(50),
	name varchar(255),
	disporder int,
	description varchar(4000),
	creator varchar(30),
	createdtime timestamp,
	modifier varchar(30),
	modifiedtime timestamp,	
	primary key (id)
);

--CREATE TABLE swcategoryextprop(
--	id varchar(50) NOT NULL,
--	ctgname varchar(100) NULL,
--	ctgvalue varchar(100) NULL,
--	ctgseq int NOT NULL,
--	primary key (id, ctgseq)
--);

CREATE TABLE swpackage (
	id varchar(50) NOT NULL,
	categoryid varchar(50),
	packageid varchar(50),
	version int,
	name varchar(100),
	type varchar(20),
	status varchar(30),
	latestdeployedyn char(1),
	helpUrl varchar(500),
	manualFileName varchar(100),
	creator varchar(30),
	createdtime timestamp,
	modifier varchar(30),
	modifiedtime timestamp,
	description varchar(4000),
	primary key (id)
);

CREATE TABLE swprocess (
	id varchar(50) NOT NULL,
	packageid varchar(50),
	processid varchar(50),
	version int,
	name varchar(100),
	status varchar(30),
	publishmode varchar(10),
	keyword varchar(255),	
	creator varchar(30),
	createdtime timestamp,
	modifier varchar(30),
	modifiedtime timestamp,
	ownerdept varchar(30),
	owner varchar(30),
	encoding varchar(15),
	description varchar(4000),
	content clob,
	primary key (id)
);

CREATE TABLE swform (
	id varchar(50) NOT NULL,
	packageid varchar(50),
	formid varchar(50),
	version int,
	name varchar(100),
	type varchar(20),
	status varchar(30),
	publishmode varchar(10),
	keyword varchar(255),
	creator varchar(30),
	createdtime timestamp,
	modifier varchar(30),
	modifiedtime timestamp,
	ownerdept varchar(30),
	owner varchar(30),
	encoding varchar(15),
	description varchar(4000),
	content clob,
	primary key (id)
);

CREATE TABLE swworktype (
	id varchar(50) NOT NULL,
	name varchar(255),
	formuid varchar(50),
	stepcount int,
	type varchar(30),
	duration int,
	primary key (id)
);

--CREATE TABLE swworktypeperformerline (
--	worktypeid varchar(50) NOT NULL,
--	step int,
--	performertype int,
--	performer varchar(30),
--	primary key (worktypeid, step)
--);

-- 의견
CREATE TABLE swopinion (
	id varchar(50) NOT NULL,
	reftype int,
	groupid varchar(50),
	refid varchar(50),
	domainid varchar(400),
	formid varchar(400),
	title varchar(255),
	opinion varchar(4000),
	writer varchar(30),
	writtentime timestamp,
	modifier varchar(30),
	modifiedTime timestamp,
	primary key (id)	
);

---- qna
--CREATE TABLE swqna (
--	id varchar(50) NOT NULL,
--	parentid varchar(50),
--	reftype int,
--	refid varchar(50),
--	writer varchar(30),
--	writtentime timestamp,
--	title varchar(255),
--	content varchar(4000),
--	primary key (id)
--);

-- tag : 구성 바꿔야 함... TO-D0 - taskObjId와 매핑될 수 있도록 구성 
CREATE TABLE swtag (
	id varchar(50) NOT NULL,
	userid varchar(30),
	tag varchar(50),
	createdtime timestamp,
	primary key (id)
);

--CREATE TABLE swpost (
--	id varchar(50) NOT NULL,
--	tagid varchar(50),
--	reftype int,
--	refgroupid varchar(50),
--	refid varchar(50),
--	typename varchar(255),
--	title varchar(4000),
--	createdtime timestamp,
--	primary key (id)
--);

---- 문서관리 : 단위 + 시스템
--CREATE TABLE swdocument (
--  	id varchar(50) NOT NULL,
--  	title varchar(50),
--  	code varchar(50),
--	ownerdept varchar(50),
--	owneruser varchar(50),
--  	filegroupid varchar(255),
--  	reftype int,
--  	refid varchar(50),
--	domainid varchar(50),
--	workitemid varchar(50),
--	creator	varchar(50),
--	createdtime timestamp,
--	modifier	varchar(50),
--	modifiedtime timestamp,
--  	primary key(id)
--);

CREATE TABLE swfile (
  	id varchar(50) NOT NULL,	  
  	type varchar(10),	  
  	filename varchar(255),
  	filepath varchar(1000),  
	filesize int,
	writtentime timestamp,
	deleteAction Number(3),
  	primary key(id)
);

CREATE TABLE swdocgroup (
	tskInstanceId varchar(50) NOT NULL,
  	groupId varchar(50) NOT NULL,
  	docId varchar(50) NOT NULL,
  	primary key(groupId, docId)
);

---- 프로세스 엔진
--CREATE TABLE swactinstprop (
--	id varchar(50) NOT NULL,
--	name varchar(100) NOT NULL,
--	data varchar(2000),
--	primary key (id, name)
--);

--CREATE TABLE swactinst (
--	id varchar(50) NOT NULL,
--	prcinstid varchar(50),		
--	repeatcount int,
--	loopcount int,
--	exestep int,
--	uptodateyn char(1),
--	activityid varchar(50),
--	name varchar(50),		
--	status varchar(30),
--	type int,
--	apprefid varchar(50),
--	starttime timestamp,
--	endtime timestamp,
--	primary key (id)
--);

--CREATE TABLE swprcinst (
--	id varchar(50) NOT NULL,
--	processid varchar(50),
--	version int,
--	title varchar(255),
--	type int,
--	step int,
--	status varchar(30),
--	initiator varchar(50),
--	starttime timestamp,
--	endtime timestamp,
--	bizstatus varchar(50),
--	keyword varchar(100),
--	description varchar(4000),
--	primary key (id)
--);

--CREATE TABLE swprcinstprop (
--	id varchar(50) NOT NULL,
--	step int NOT NULL,
--	name varchar(100) NOT NULL,
--	data varchar(4000),
--	primary key (id, step)
--);

--CREATE TABLE swvarinst (
--	prcinstid varchar(50) NOT NULL,
--	refid varchar(50) NOT NULL,
--	name varchar(50) NOT NULL,
--	type int,
--	datatype varchar(50),
--	data char,
--	primary key (prcinstid, refid, name)
--);

--CREATE TABLE swruntimeerror (
--	id varchar(50) NOT NULL,
--	processid varchar(50),
--	version int,
--	prcinstid varchar(50),
--	actinstid varchar(50),
--	status int,
--	step int,
--	performer varchar(50),
--	reactor varchar(50),
--	source varchar(50),
--	type varchar(255),
--	trace varchar(2000),
--	occurredtime timestamp,
--	reactedtime timestamp,
--	message char,
--	primary key (id)
--);

---- 워크리스트
--CREATE TABLE swworkitem (
--	id varchar(50) NOT NULL,
--	worktypeid varchar(50),
--	title varchar(255),
--	status int,
--	assigner varchar(30),
--	assigneetype int,
--	assignee varchar(30),
--	performer varchar(30),
--	keyword varchar(255),
--	importance char(1),
--	priority char(1),
--	createdtime timestamp,
--	completedtime timestamp,
--	duetime timestamp,
--	previousid varchar(50),
--	type int,
--	step int,
--	refid varchar(50),
--	groupid varchar(50),
--	deletedyn char(1),
--	description varchar(4000),	
--	data char,
--	primary key (id)
--);

--CREATE TABLE swworkitementrust (
--	id varchar(50) NOT NULL,
--	workitemid varchar(50),
--	previousassignee varchar(30),
--	entruster varchar(30),
--	operator varchar(30),
--	entrustedtime timestamp,
--	primary key (id)
--);

--CREATE TABLE swworkitemparticipant (
--	id varchar(50) NOT NULL,
--	workitemid varchar(50),
--	participanttype int,
--	participant varchar(30),
--	primary key (id)
--);

--CREATE TABLE swworkitemprop (
--	workitemid varchar(50) NOT NULL,
--	name varchar(255) NOT NULL,
--	data varchar(4000),
--	primary key (workitemid, name)
--);

CREATE TABLE swworkhistory (
	id varchar(50) NOT NULL,
	userid varchar(30),
	type varchar(20),
	formid varchar(50),
	title varchar(4000),
	executedtime timestamp,
	primary key (id)
);

CREATE TABLE swworktypehistory (
	id varchar(50) NOT NULL,
	userid varchar(30),
	formid varchar(50),
	executedtime timestamp,
	primary key (id)
);

-- 도메인 데이터
CREATE TABLE swdomain (
	id varchar(50) NOT NULL,
	companyid varchar(100),
	formid varchar(50),
	formversion int,
	formname varchar(100),
	tblowner varchar(100),
	tblname varchar(100),
	keycolumn varchar(100),
	titlefieldid varchar(50),
	keyDuplicable Number(3) default '0',
	masterid varchar(50),
	masterfieldid varchar(50),
	systemdomainyn char(1),
	publishmode varchar(10),
	primary key (id)
);

CREATE TABLE swremoveddomain (
	formid varchar(50) NOT NULL,
	tblname varchar(100),
	primary key(formid)
);

CREATE TABLE swdomainfield (
	id varchar(50) NOT NULL,
	domainid varchar(50),
	formfieldid varchar(50),
	formfieldpath varchar(255),
	formfieldname varchar(255),
	formfieldtype varchar(50),
	tablecolname varchar(50),
	reftblname varchar(50),
	arrayyn char(1),
	systemfieldyn char(1),
	uniqueyn varchar(1),
	disporder int,
	tablewidth float,
	primary key (id)
);

CREATE TABLE swdataref (
	id varchar(50),
	myformid varchar(50),
	myformfieldid varchar(20),
	myrecordid varchar(50),
	refformid varchar(50),
	refformfieldid varchar(20),
	refrecordid varchar(4000),
	primary key (id)
);

-- 가젯 정보
CREATE TABLE swgadget (
	id 	varchar(50) NOT NULL,
	companyid varchar(50),
	name varchar(255),
	userid varchar(50) NOT NULL,
	createdtime timestamp,
	modifier varchar(50),
	modifiedtime timestamp,
	type varchar(50),
	formid varchar(50),
	query varchar(4000),
	classname varchar(255),
	location varchar(255),
	enabledyn char(1),
	content clob,
	primary key (id)
);

-- 권한 정보
CREATE TABLE swauthresource (
	id 	varchar(50) NOT NULL,
	companyid varchar(50),
	resourceid varchar(50) NOT NULL,	
	type int NOT NULL,
	authmode char(1) NOT NULL,
	permission varchar(20),	
	creator varchar(50),
	createdtime timestamp,
	modifier varchar(50),
	modifiedtime timestamp,
	primary key (id)
);

CREATE TABLE swauthuser (
	id varchar(50) NOT NULL,
	companyid varchar(50),		
	resourceid varchar(50) NOT NULL,
	authmode char(1) NOT NULL,
	userid varchar(50) NOT NULL,
	type varchar(20) NOT NULL,
	primary key (id)
);

-- 로고
CREATE TABLE swconfig (
	id varchar(50) NOT NULL,
	logo varchar(50),
	loginImage varchar(50),
	primary key (id)
);

---- 국제화
--CREATE TABLE swlanguage (
--	id varchar(255) NOT NULL,
--	kor varchar(255),
--	eng varchar(255),
--	primary key (id)
--);

CREATE TABLE aprapr (
    aprobjid varchar(100) NOT NULL,
    aprname varchar(100),
    aprcreateuser varchar(50),
    aprcreatedate timestamp,
    aprmodifyuser varchar(50),
    aprmodifydate timestamp,
    aprdesc clob,
    aprstatus varchar(50),
    aprtype varchar(50),
    aprapprover varchar(50),
   	aprDueDate varchar(100),
    aprismanda Number(3),
    aprismodify Number(3),
    approvalline varchar(100),
    aprseq int,
    primary key (aprobjid)
);

CREATE TABLE apraprextprop (
    aprobjid varchar(100) NOT NULL,
    aprname varchar(100),
    aprvalue varchar(100),
    aprseq int NOT NULL,
    primary key (aprobjid, aprseq)
);

CREATE TABLE apraprline (
    aprobjid varchar(100) NOT NULL,
    aprname varchar(100),
    aprcreateuser varchar(50),
    aprcreatedate timestamp,
    aprmodifyuser varchar(50),
    aprmodifydate timestamp,
    aprdesc clob,
    aprstatus varchar(50),
    aprcorr varchar(50),
    aprRefAppLineDefId varchar(50),
	primary key (aprobjid)
);

CREATE TABLE apraprlinedef(
	objid varchar(50) NOT NULL,
	aprlinename varchar(255) NULL,
	aprdescription varchar(50) NULL,
	aprlevel varchar(50) NULL,
	companyid varchar(255) NULL,
	creator varchar(50) NULL,
	createdtime timestamp NULL,
	modifier varchar(50) NULL,
	modifiedtime timestamp NULL,
	primary key (objid)
);

CREATE TABLE apraprlineextprop (
    aprobjid varchar(100) NOT NULL,
    aprname varchar(100),
    aprvalue varchar(100),
    aprseq int NOT NULL,
	primary key (aprobjid, aprseq)
);

--CREATE TABLE chtchart (
--    chtobjid varchar(100) NOT NULL,
--    chtcharttype varchar(20) NOT NULL,
--    chtname varchar(50),
--    chtcreateuser varchar(255),
--    chtcreatedate timestamp,
--    chtmodifyuser varchar(255),
--    chtmodifydate timestamp,
--    chtobjstr char
--);

--CREATE TABLE chtcharcharprop (
--    chtobjid varchar(100) NOT NULL,
--    chtname varchar(100),
--    chtvalue varchar(100),
--    chtseq int NOT NULL
--);

CREATE TABLE collist (
    colobjid varchar(100) NOT NULL,
    colname varchar(100),
    colcreateuser varchar(50),
    colcreatedate timestamp,
    colmodifyuser varchar(50),
    colmodifydate timestamp,
    coltype varchar(100),
    colcorr varchar(200),
    colstatus varchar(50),
    coldesc clob,
    primary key(colobjid)
);

CREATE TABLE collistextprop (
    colobjid varchar(100) NOT NULL,
    tskname varchar(100),
    tskvalue varchar(100),
    colseq int NOT NULL,
    primary key(colobjid, colseq)
);

CREATE TABLE collistitem (
    colobjid varchar(100) NOT NULL,
    coltype varchar(100),
    colref varchar(100),
    collabel varchar(100),
    colexpr clob,
    colseq int NOT NULL,
    primary key(colobjid, colseq)
);

CREATE TABLE lnklnk (
    lnkobjid varchar(100) NOT NULL,
    lnkname varchar(100),
    lnkcreateuser varchar(50),
    lnkcreatedate timestamp,
    lnkmodifyuser varchar(50),
    lnkmodifydate timestamp,
    lnktype varchar(100),
    lnkcorr varchar(400),
    lnkfromtype varchar(100),
    lnkfromref varchar(200),
    lnkfromlabel varchar(100),
    lnkfromexpr clob,
    lnktotype varchar(100),
    lnktoref varchar(200),
    lnktolabel varchar(100),
    lnktoexpr clob,
    lnkcondtype varchar(100),
    lnkcondexpr clob,
    primary key(lnkobjid)
);

CREATE TABLE lnklnkextprop (
    lnkobjid varchar(100) NOT NULL,
    tskname varchar(100),
    tskvalue varchar(100),
    lnkseq int NOT NULL,
    primary key(lnkobjid, lnkseq)
);

CREATE TABLE colmap (
    colobjid varchar(100) NOT NULL,
    colname varchar(100),
    colcreateuser varchar(50),
    colcreatedate timestamp,
    colmodifyuser varchar(50),
    colmodifydate timestamp,
    coltype varchar(200),
    colfromtype varchar(200),
    colfromref varchar(200),
    coltotype varchar(200),
    coltoref varchar(200),
    primary key(colobjid)
);

CREATE TABLE colmapextprop (
    colobjid varchar(100) NOT NULL,
    tskname varchar(100),
    tskvalue varchar(100),
    colseq int NOT NULL,
    primary key(colobjid, colseq)
);

CREATE TABLE colvalue (
    colobjid varchar(100) NOT NULL,
    colname varchar(100),
    colcreateuser varchar(50),
    colcreatedate timestamp,
    colmodifyuser varchar(50),
    colmodifydate timestamp,
    coltype varchar(100),
    colref varchar(200),
    colexpdate timestamp,
    colstatus varchar(50),
    coldesc clob,
    colvalue clob,
    primary key(colobjid)
);

CREATE TABLE colvalueextprop (
    colobjid varchar(100) NOT NULL,
    tskname varchar(100),
    tskvalue varchar(100),
    colseq int NOT NULL,
    primary key(colobjid, colseq)
);

--CREATE TABLE mdlmodel (
--    mdlobjid varchar(100) NOT NULL,
--    mdlname varchar(50),
--    mdlcreateuser varchar(255),
--    mdlcreatedate timestamp,
--    mdlmodifyuser varchar(255),
--    mdlmodifydate timestamp,
--    mdlpkg varchar(50),
--    mdlprefix varchar(50),
--    mdlobjstr char,
--	  primary key(mdlobjid)
--);

--CREATE TABLE mdlmodelextprop (
--    mdlobjid varchar(100) NOT NULL,
--    mdlname varchar(100),
--    mdlvalue varchar(100),
--    mdlseq int NOT NULL,
--	  primary key(mdlobjid, mdlseq)
--);

CREATE TABLE prcprc (
    prcobjid varchar(100) NOT NULL,
    prcname varchar(100),
    prccreateuser varchar(50),
    prccreatedate timestamp,
    prcmodifyuser varchar(50),
    prcmodifydate timestamp,
    tskdesc clob,
    prcdid varchar(100),
    prcdver varchar(100),
    prcprcid varchar(100),
    prcdiagram clob,
    prcType varchar(100),
	primary key(prcobjid)
);

CREATE TABLE prcprcextprop (
    prcobjid varchar(100) NOT NULL,
    prcname varchar(100),
    prcvalue varchar(100),
    prcseq int NOT NULL,
	primary key(prcobjid, prcseq)
);

CREATE TABLE prcprcinst (
    prcobjid varchar(100) NOT NULL,
    prcname varchar(100),
    prccreateuser varchar(50),
    prccreatedate timestamp,
    prcmodifyuser varchar(50),
    prcmodifydate timestamp,
    prcstatus varchar(100),
    prctitle varchar(255),
    tskdesc clob,
    tskpriority varchar(50),
    prcdid varchar(100),
    prcdver varchar(100),
    prcprcid varchar(100),
    prcdiagram clob,
    issubinstance varchar(50) NULL,
    prcinstvariable clob NULL,
    prcPackageId varchar(100),
    prcType varchar(100),
    prcWorkspaceId varchar(100),
    prcWorkSpaceType varchar(50),
    prcAccessLevel varchar(50),
    prcAccessValue varchar(4000),
	primary key(prcobjid)
);

CREATE TABLE prcprcinstextprop (
    prcobjid varchar(100) NOT NULL,
    prcname varchar(100),
    prcvalue varchar(100),
    prcseq int NOT NULL,
	primary key(prcobjid, prcseq)
);

CREATE TABLE tsktask (
    tskobjid varchar(100) NOT NULL,
    tskname varchar(100),
    tskcreateuser varchar(50),
    tskcreatedate timestamp,
    tskmodifyuser varchar(50),
    tskmodifydate timestamp,
    tskstatus varchar(50),
    tskcorr varchar(50),
    tsktype varchar(100),
    tskprcinstid varchar(50),
    tsktitle varchar(255),
    tskdesc clob,
    tskpriority varchar(50),
    tskdoc clob,
    tskassigner varchar(50),
    tskassignee varchar(50),
    tskperformer varchar(50),
    tskstartdate timestamp,
    tskassigndate timestamp,
    tskexecutedate timestamp,
    tskduedate timestamp,
    tskdef varchar(100),
    tskform varchar(100),
    tskmultiinstid varchar(100),
    tskmultiinstorder varchar(10),
    tskmultiinstflowcond varchar(10),
    tskstep int,
    tskloopcnt int,
    tskexpectstartdate timestamp NULL ,
    tskexpectenddate timestamp NULL ,
    tskrealstartdate timestamp NULL ,
    tskrealenddate timestamp NULL ,
    tskinstvariable clob NULL,
    isStartActivity varchar(10),
    tskFromRefType varchar(50),
    tskFromRefId varchar(100),
    tskApprovalId varchar(100),
    tskForwardId varchar(100),
    tskIsApprovalSourceTask varchar(10),
    tskTargetApprovalStatus varchar(10),
    tskWorkspaceId varchar(100),
    tskWorkspaceType varchar(50),
    tskAccessLevel varchar(50),
    tskAccessValue varchar(4000),
    tskRefType varchar(100),
	primary key(tskobjid)
);


CREATE TABLE tsktaskdef (
    tskobjid varchar(100) NOT NULL,
    tskname varchar(100),
    tskcreateuser varchar(50),
    tskcreatedate timestamp,
    tskmodifyuser varchar(50),
    tskmodifydate timestamp,
    tskstatus varchar(50),
    tskcorr varchar(100),
    tsktype varchar(100),
    tskprcinstid varchar(100),
    tsktitle varchar(200),
    tskdesc clob,
    tskpriority varchar(100),
    tskdoc clob,
    tskassigner varchar(100),
    tskassignee varchar(100),
    tskassigndate varchar(100),
    tskduedate varchar(100),
    tskform varchar(100),
    tskmultiinstorder varchar(10),
    tskmultiinstflowcond varchar(10),
    tsksubflowtargetid	varchar(200) NULL,
	tsksubflowtargetver	varchar(10) NULL,
	tsksubflowexecution	varchar(50) NULL,
    tskservicetargetid varchar(100) NULL,
	primary key(tskobjid)
);

CREATE TABLE tsktaskdefextprop (
    tskobjid varchar(100) NOT NULL,
    tskname varchar(100),
    tskvalue varchar(100),
    tskseq int NOT NULL,
	primary key(tskobjid, tskseq)
);

CREATE TABLE tsktaskextprop (
    tskobjid varchar(100) NOT NULL,
    tskname varchar(100),
    tskvalue varchar(800),
    tskseq int NOT NULL,
	primary key(tskobjid, tskseq)
);

-- 공지사항
CREATE TABLE SWNotice (
	id varchar(50) NOT NULL,
	domainId varchar(50) NULL,
	workItemId varchar(50) NULL,
	masterRecordId varchar(50) NULL,
	creator varchar(50) NULL,
	createdTime timestamp NULL,
	modifier varchar(50) NULL,
	modifiedTime timestamp NULL,
	filegroupid varchar(255) NULL,
	title varchar(255) NULL,
	content clob NULL,
	duration timestamp null,
	primary key (id)
);

-- 게시판
CREATE TABLE SwBoard (
	id 	varchar(50) NOT NULL,
	title varchar(255),
	priority varchar(10),
	content clob,
	filegroupid varchar(255),
	domainid varchar(50),
	workitemid varchar(50),
	creator	varchar(50),
	createdtime timestamp,
	modifier	varchar(50),
	modifiedtime timestamp,
	notice Number(3),
	primary key (id)
);

-- 자료실
CREATE TABLE SWAttachment (
	id varchar(50) NOT NULL,
	domainid varchar(50) NULL,
	workitemid varchar(50) NULL,
	masterrecordid varchar(50) NULL,
	creator varchar(50) NULL,
	createdtime timestamp NULL,
	modifier varchar(50) NULL,
	modifiedtime timestamp NULL,
	content clob NULL,
	filegroupid varchar(255) NULL,
	searchword varchar(255) NULL,
	ownerdept varchar(255) NULL,
	title varchar(255) NULL,
	owneruser varchar(255) NULL
);

-- 연락처
CREATE TABLE sworgcontact (
	id varchar(50) NOT NULL,
	name varchar(50),
	companyid varchar(50),
	deptid varchar(50),
	pos varchar(50),
	email varchar(100),
	telephone varchar(50),
	swcompphone varchar(50),
	swfax varchar(50),
	swaddress varchar(255),
	swcountry varchar(50),
	swcat varchar(50),
	swdesc clob,
	swattach varchar(255),
	domainid varchar(50),
	workitemid varchar(50),
	creator	varchar(50),
	createdtime timestamp,
	modifier	varchar(50),
	modifiedtime timestamp,
	primary key (id)
);

-- 이벤트
CREATE TABLE SWEvent (
	id varchar(50) NOT NULL,
	domainId varchar(50) NULL,
	workItemId varchar(50) NULL,
	masterRecordId varchar(50) NULL,
	creator varchar(50) NULL,
	createdTime timestamp NULL,
	modifier varchar(50) NULL,
	modifiedTime timestamp NULL,
	enddate timestamp NULL,
	relatedusers varchar(4000) NULL, -- userField는 varchar(4000)으로 생성
	content clob NULL,
	startdate timestamp NULL,
	name varchar(255) NULL,
	place varchar(255) NULL,
	alarm varchar(255) NULL,
	primary key (id)
);

-- 메모
CREATE TABLE SWMemo (
	id varchar(50) NOT NULL primary key,
	domainid varchar(50),
	workitemid varchar(50),
	masterrecordid varchar(50),
	creator varchar(50),
	createdtime timestamp,
	modifier varchar(50),
	modifiedtime timestamp,
	content clob,
	title varchar(255)
);

----업무전달
--CREATE TABLE dt_1287553898892 (
--	id varchar(50) NOT NULL,
--	domainid varchar(50) NULL,
--	workitemid varchar(50) NULL,
--	masterrecordid varchar(50) NULL,
--	creator varchar(50) NULL,
--	createdtime timestamp NULL,
--	modifier varchar(50) NULL,
--	modifiedtime timestamp NULL,
--	c8 char NULL,
--	c12 varchar(255) NULL,
--	c4 varchar(255) NULL
--);

----업무보고
--CREATE TABLE dt_1287553898894(
--	id varchar(50)NOT NULL,
--	domainid varchar(50),
--	workitemid varchar(50),
--	masterrecordid varchar(50),
--	creator varchar(50),
--	createdtime timestamp ,
--	modifier varchar(50),
--	modifiedtime timestamp ,
--	c15 char,
--	c2 varchar(255),
--	c6 varchar(255),
--	c4 varchar(255),
--	c19 varchar(255),
--	c61 timestamp ,
--	c17 char,
--	c13 char,
--	c0 varchar(255),
--	c63 timestamp ,
--	c8 timestamp 
--);

--sw2.0 추가
CREATE TABLE swmenuitem(
	objid varchar(50) NOT NULL,
	name varchar(255) NULL,
	menuseqno int NULL,
	imgpath varchar(150) NULL,
	categoryid varchar(50) NULL,
	packageid varchar(50) NULL,
	packagetype varchar(50) NULL,
	groupid varchar(50) NULL,
	formid varchar(50) NULL,
	creator varchar(50) NULL,
	createdtime timestamp NULL,
	modifier varchar(50) NULL,
	modifiedtime timestamp NULL,
	itmseq int NOT NULL,
	primary key (objid, itmseq)
) 
;

CREATE TABLE swmenuitemlist(
	objid varchar(50) NOT NULL ,
	companyid varchar(50) NULL,
	userid varchar(50) NULL,
	creator varchar(50) NULL,
	createdtime timestamp NULL,
	modifier varchar(50) NULL,
	modifiedtime timestamp NULL,
	primary key (objid)
)
;

CREATE TABLE sweventday(
	objid varchar(50) NOT NULL,
	name varchar(50),
	companyid varchar(100),
	type varchar(50),
	startday timestamp,
	endday timestamp,
	description varchar(4000),
	creator varchar(50),
	createdtime timestamp,
	modifier varchar(50),
	modifiedtime timestamp NULL,
	reltdperson varchar(4000),
	primary key (objid)
)
;

CREATE TABLE sworgteam(
	id varchar(50) NOT NULL,
	companyid varchar(50) NULL,
	name varchar(255) NULL,
	teamleader varchar(50) NULL,
	dept varchar(50) NULL,
	member clob NULL,
	accesslevel varchar(50) NULL,
	state varchar(50) NULL,
	description varchar(4000) NULL,
	creator varchar(50) NULL,
	createdtime timestamp NULL,
	modifier varchar(50) NULL,
	modifiedtime timestamp NULL,
	primary key (id)
)
;

CREATE TABLE msgmessage(
	id varchar(50) NOT NULL,
	content varchar(2000) NULL,
	targetuser varchar(300) NULL,
	userid varchar(50) NULL,
	modifier varchar(50) NULL,
	modifiedtime timestamp NULL,
	createdtime timestamp NULL,
	primary key (id)
)
;

CREATE TABLE swworkhour (
	objid varchar(50) NOT NULL,
	companyid varchar(50),
	type varchar(50),
	startdayofweek varchar(50),
	workingdays int,
	validfromdate timestamp,
	validtodate timestamp,
	monstarttime timestamp,
	monendtime timestamp,
	tuestarttime timestamp,
	tueendtime timestamp,
	wedstarttime timestamp,
	wedendtime timestamp,
	thustarttime timestamp,
	thuendtime timestamp,
	fristarttime timestamp,
	friendtime timestamp,
	satstarttime timestamp,
	satendtime timestamp,
	sunstarttime timestamp,
	sunendtime timestamp,
	creator varchar(50),
	createdtime timestamp,
	modifier varchar(50),
	modifiedtime timestamp,
	primary key (objid)
)
;

CREATE TABLE sworgconfig(
	id varchar(50) NOT NULL,
	smtpaddress varchar(100) NULL,
	userid varchar(50) NULL,
	password varchar(20) NULL,
	name varchar(255) NULL,
	domainid varchar(50) NULL,
	companyid varchar(100) NULL,
	creator varchar(50) NULL,
	createdtime timestamp NULL,
	modifier varchar(50) NULL,
	modifiedtime timestamp NULL,
	isactivity int NULL,
	setupcompanydate timestamp,
	userreturnfunction int,
	usechattingservice int,
	usemessagingservice int,
	primary key (id)
)
;

--추가
CREATE TABLE apraprdef(  
	objid varchar(50),
	type varchar(255),
	aprname varchar(150),
	aprperson varchar(50),
	dueDate varchar(100),
	level_ varchar(50),
	creator varchar(50),
	createdtime timestamp NULL,
	modifier varchar(50),
	modifiedtime timestamp NULL,
	defseq int NOT NULL,
	constraint apraprdef_pkey primary key (objid, defseq),
	constraint fkaprapprovaldef foreign key (objid) references apraprlinedef (objid)
);


--CREATE TABLE docfile(  
--	id varchar(50),
--	companyid varchar(30),
--	type varchar(255),
--	filename varchar(100),
--	filepath varchar(200),
--	filesize decimal(19, 0) NULL,
--	writtentime timestamp NULL,
--	constraint docfile_pkey primary key (id)
--);


--CREATE TABLE lcslcs(  
--	lcsobjid varchar(100),
--	lcsname varchar(100),
--	lcscreateuser varchar(50),
--	lcscreatedate timestamp NULL,
--	lcsmodifyuser varchar(50),
--	lcsmodifydate timestamp NULL,
--	lcsusername varchar(50),
--	lcsprod varchar(50),
--	lcscurrentver varchar(50),
--	lcsprodkey varchar(100),
--	lcsdomain varchar(100),
--	lcsmacaddr varchar(100),
--	lcsusersize decimal(19, 0) NULL,
--	lcsexpiredate timestamp NULL,
--	lcsdesc char,
--	lcsstatus varchar(50),
--	constraint lcslcs_pkey primary key (lcsobjid)
--);


--CREATE TABLE lcslcsextprop(
--	lcsobjid varchar(100),
--	lcsname varchar(100),
--	lcsvalue varchar(100),
--	lcsseq int NOT NULL,
--	constraint lcslcsextprop_pkey primary key (lcsobjid, lcsseq),
--	constraint fklcslcsextprop foreign key (lcsobjid)
--	references lcslcs (lcsobjid)
--);


CREATE TABLE prcprcinstrel(
	prcobjid varchar(100),
	prccreateuser varchar(50),
	prccreatedate timestamp NULL,
	prcmodifyuser varchar(50),
	prcmodifydate timestamp NULL,
	prctype varchar(50),
	prcinstanceid varchar(100),
	prcparentinstid varchar(100),
	constraint prcprcinstrel_pkey primary key (prcobjid)
);


--CREATE TABLE prcprcinstrelextprop(  
--	prcobjid varchar(100),
--	prcname varchar(100),
--	prcvalue varchar(100),
--	prcseq int NOT NULL,
--	constraint prcprcinstrelextprop_pkey primary key (prcobjid, prcseq),
--	constraint fkprcprcinstrelextprop foreign key (prcobjid)
--	references prcprcinstrel (prcobjid)
--);


--CREATE TABLE prcprcinstvariable(  
--	prcobjid varchar(100),
--	prcname varchar(100),
--	prccreateuser varchar(50),
--	prccreatedate timestamp NULL,
--	prcmodifyuser varchar(50),
--	prcmodifydate timestamp NULL,
--	prctype varchar(50),
--	prcprcinstid varchar(100),
--	prcvariableid varchar(100),
--	prcrequired tinyint NULL,
--	prcmode varchar(50),
--	prcvalue char,
--        constraint prcprcinstvariable_pkey primary key (prcobjid)
--);


--CREATE TABLE prcprcinstvariableextprop(  
--	prcobjid varchar(100),
--	prcname varchar(100),
--	prcvalue varchar(100),
--	prcseq int NOT NULL,
--	constraint prcprcinstvariableextprop_pkey primary key (prcobjid, prcseq),
--	constraint fkprcprcinstvariableextprop foreign key (prcobjid)
--	references prcprcinstvariable (prcobjid)
--);


--CREATE TABLE prcprcvariable(
--	prcobjid varchar(100),
--	prcname varchar(100),
--	prccreateuser varchar(50),
--	prccreatedate timestamp NULL,
--	prcmodifyuser varchar(50),
--	prcmodifydate timestamp NULL,
--	prctype varchar(50),
--	prcprcid varchar(100),
--	prcinitialvalue varchar(100),
--	prcrequired tinyint NULL,
--	prcmode varchar(50),
--	constraint prcprcvariable_pkey primary key (prcobjid)
--);


--CREATE TABLE prcprcvariableextprop(  
--	prcobjid varchar(100),
--	prcname varchar(100),
--	prcvalue varchar(100),
--	prcseq int NOT NULL,
--	constraint prcprcvariableextprop_pkey primary key (prcobjid, prcseq),
--	constraint fkprcprcvariableextprop foreign key (prcobjid)
--	references prcprcvariable (prcobjid)
--);


--CREATE TABLE swcustomer(
--	id varchar(50),
--	status varchar(30),
--	name varchar(255),
--	customercode varchar(100),
--	smartworksurl varchar(200),
--	description char,
--	creator varchar(50),
--	createdtime timestamp NULL,
--	modifier varchar(50),
--	modifiedtime timestamp NULL,
--	constraint swcustomer_pkey primary key (id)
--);

  
--CREATE TABLE swcustomerextprop(
--	id varchar(50),
--	swcname varchar(100),
--	swcvalue varchar(100),
--	swcseq int NOT NULL,
--	constraint swcustomerextprop_pkey primary key (id, swcseq),
--	constraint fkswccustomerextprop foreign key (id)
--	references swcustomer (id)
--);

  
CREATE TABLE sworguser_backup(  
	id varchar(50),
	companyid varchar(50),
	deptid varchar(50),
	roleid varchar(50),
	authid varchar(50),
	empno varchar(50),
	name varchar(255),
	type varchar(50),
	pos varchar(50),
	email varchar(50),
	passwd varchar(20),
	lang varchar(20),
	stdtime varchar(20),
	picture varchar(50),
	domainid varchar(50),
	workitemid varchar(50),
	creator varchar(50),
	createdtime timestamp NULL,
	modifier varchar(50),
	modifiedtime timestamp NULL,
	retiree varchar(50),
	constraint sworguser_backup_pkey primary key (id)
);


CREATE TABLE sworguserdetail(  
	id varchar(50),
	companyid varchar(50),
	empno varchar(50),
	name varchar(255),
	telcompany varchar(50),
	mobile varchar(50),
	telhome varchar(50),
	constraint sworguserdetail_pkey primary key (id)
);


--CREATE TABLE swproduct(  
--	id varchar(50),
--	name varchar(255),
--	creator varchar(50),
--	createdtime timestamp NULL,
--	modifier varchar(50),
--	modifiedtime timestamp NULL,
--	description char,
--	status varchar(30),
--	type varchar(20),
--	company varchar(100),
--	price varchar(50),
--	score float NULL,
--	mainimg varchar(255),
--	value char,
--	businesstypectgid varchar(100),
--	businessctgid varchar(100),
--	hitcount decimal(19, 0) NULL,
--	downcount decimal(19, 0) NULL,
--	ispublished varchar(20),
--	productcode varchar(50),
--	packagerels char,
--	extvalue char,
--	constraint swproduct_pkey primary key (id)
--);


--CREATE TABLE swproduccharprop(  
--	id varchar(50),
--	swmname varchar(100),
--	swmvalue varchar(100),
--	swmseq int NOT NULL,
--	constraint swproduccharprop_pkey primary key (id, swmseq),
--	constraint fkswmproduccharprop foreign key (id)
--	references swproduct (id)
--);


--CREATE TABLE swproductfile(  
--	id varchar(50),
--	type varchar(20),
--	productid varchar(100),
--	filetype varchar(20),
--	filename varchar(100),
--	filepath varchar(200),
--	filesize varchar(50),
--	creator varchar(50),
--	createdtime timestamp NULL,
--	modifier varchar(50),
--	modifiedtime timestamp NULL,	
--	constraint swproductfile_pkey primary key (id)
--);


--CREATE TABLE swproductprop(  
--	id varchar(50),
--	swmtype varchar(20),
--	swmname varchar(100),
--	swmimageid varchar(100),
--	swmdesc char,
--	swmimage char,
--	swmimagetn char,
--	swmseq int NOT NULL,
--	constraint swproductprop_pkey primary key (id, swmseq),
--	constraint fkswmproductproperty foreign key (id)
--	references swproduct (id)
--);


CREATE TABLE swwebappservice(  
	objid varchar(50),
	webappservicename varchar(100),
	webappserviceurl varchar(100),
	modifymethod varchar(100),
	viewmethod varchar(100),
	description varchar(100),
	companyid varchar(100),
	constraint swwebappservice_pkey primary key (objid)
);


CREATE TABLE swwebappserviceparameter(  
	objid varchar(50),
	variablename varchar(150),
	parametername varchar(255),
	parametertype varchar(150),
	type varchar(50),
	webseq int NOT NULL,
	constraint swwebappserviceparameter_pkey primary key (objid, webseq),
	constraint fkwebappservice foreign key (objid)
	references swwebappservice (objid)
);


CREATE TABLE swwebservice(  
	objid varchar(50),
	webservicename varchar(100),
	webserviceaddress varchar(100),
	wsdladdress varchar(100),
	portname varchar(100),
	operationname varchar(100),
	description varchar(100),
	companyid varchar(100),
	constraint swwebservice_pkey primary key (objid)
);


CREATE TABLE swwebserviceparameter(  
	objid varchar(50),
	variablename varchar(150),
	parametername varchar(255),
	parametertype varchar(150),
	type varchar(50),
	webseq int NOT NULL,
	constraint swwebserviceparameter_pkey primary key (objid, webseq),
	constraint fkwebservice foreign key (objid)
	references swwebservice (objid)
);

--CREATE TABLE updupd(  
--	updobjid varchar(100),
--	updname varchar(100),
--	updcreateuser varchar(50),
--	updcreatedate timestamp NULL,
--	updmodifyuser varchar(50),
--	updmodifydate timestamp NULL,
--	upddesc char,
--	updstatus varchar(50),
--	updtype varchar(50),
--	updmethod varchar(50),
--	updsrc char,
--	updtgt char,	
--	constraint updupd_pkey primary key (updobjid)
--);
--
--
--CREATE TABLE updupdextprop(  
--	updobjid varchar(100),
--	updname varchar(100),
--	updvalue varchar(100),
--	updseq int NOT NULL,
--	constraint updupdextprop_pkey primary key (updobjid, updseq),
--	constraint fkupdupdextprop foreign key (updobjid)
--	references updupd (updobjid)
--);

-- 사용자 업무접근 로그 (2011.10.13 Add)
--CREATE TABLE SWActionLog(
--	userId varchar(50) NOT NULL,
--	userName varchar(50) NULL,
--	deptName varchar(50) NULL,
--	position varchar(50) NULL,
--	categoryName varchar(100) NULL,
--	menuName varchar(100) NULL,
--	menuType varchar(100) NULL,
--	actionType varchar(50) NULL,
--	actionTime timestamp NULL
--);

--ALTER TABLE [dbo].[AprApr] WITH NOCHECK ADD 
--	 PRIMARY KEY  CLUSTERED 
--	(
--		[aprObjId]
--	)  ON [PRIMARY] 
--GO

--ALTER TABLE [dbo].[AprAprExtProp] WITH NOCHECK ADD 
--	 PRIMARY KEY  CLUSTERED 
--	(
--		[aprObjId],
--		[aprSeq]
--	)  ON [PRIMARY] 
--GO

--ALTER TABLE [dbo].[AprAprLine] WITH NOCHECK ADD 
--	 PRIMARY KEY  CLUSTERED 
--	(
--		[aprObjId]
--	)  ON [PRIMARY] 
--GO

--ALTER TABLE [dbo].[AprAprLineExtProp] WITH NOCHECK ADD 
--	 PRIMARY KEY  CLUSTERED 
--	(
--		[aprObjId],
--		[aprSeq]
--	)  ON [PRIMARY] 
--GO

--ALTER TABLE [ChtChart] WITH NOCHECK ADD 
--	 PRIMARY KEY  CLUSTERED 
--	(
--		[chtObjId]
--	)  ON [PRIMARY] 
--GO

--ALTER TABLE [ChtCharcharProp] WITH NOCHECK ADD 
--	 PRIMARY KEY  CLUSTERED 
--	(
--		[chtObjId],
--		[chtSeq]
--	)  ON [PRIMARY] 
--GO

--ALTER TABLE [LnkList] WITH NOCHECK ADD 
--	 PRIMARY KEY  CLUSTERED 
--	(
--		[lnkObjId]
--	)  ON [PRIMARY] 
--GO

--ALTER TABLE [LnkLischarProp] WITH NOCHECK ADD 
--	 PRIMARY KEY  CLUSTERED 
--	(
--		[lnkObjId],
--		[lnkSeq]
--	)  ON [PRIMARY] 
--GO

--ALTER TABLE [LnkListItem] WITH NOCHECK ADD 
--	 PRIMARY KEY  CLUSTERED 
--	(
--		[lnkObjId],
--		[lnkSeq]
--	)  ON [PRIMARY] 
--GO

--ALTER TABLE [LnkLnk] WITH NOCHECK ADD 
--	 PRIMARY KEY  CLUSTERED 
--	(
--		[lnkObjId]
--	)  ON [PRIMARY] 
--GO

--ALTER TABLE [LnkLnkExtProp] WITH NOCHECK ADD 
--	 PRIMARY KEY  CLUSTERED 
--	(
--		[lnkObjId],
--		[lnkSeq]
--	)  ON [PRIMARY] 
--GO

--ALTER TABLE [LnkMap] WITH NOCHECK ADD 
--	 PRIMARY KEY  CLUSTERED 
--	(
--		[lnkObjId]
--	)  ON [PRIMARY] 
--GO

--ALTER TABLE [LnkMapExtProp] WITH NOCHECK ADD 
--	 PRIMARY KEY  CLUSTERED 
--	(
--		[lnkObjId],
--		[lnkSeq]
--	)  ON [PRIMARY] 
--GO

--ALTER TABLE [LnkValue] WITH NOCHECK ADD 
--	 PRIMARY KEY  CLUSTERED 
--	(
--		[lnkObjId]
--	)  ON [PRIMARY] 
--GO

--ALTER TABLE [LnkValueExtProp] WITH NOCHECK ADD 
--	 PRIMARY KEY  CLUSTERED 
--	(
--		[lnkObjId],
--		[lnkSeq]
--	)  ON [PRIMARY] 
--GO

--ALTER TABLE [MdlModel] WITH NOCHECK ADD 
--	 PRIMARY KEY  CLUSTERED 
--	(
--		[mdlObjId]
--	)  ON [PRIMARY] 
--GO

--ALTER TABLE [MdlModelExtProp] WITH NOCHECK ADD 
--	 PRIMARY KEY  CLUSTERED 
--	(
--		[mdlObjId],
--		[mdlSeq]
--	)  ON [PRIMARY] 
--GO

--ALTER TABLE [PrcPrc] WITH NOCHECK ADD 
--	 PRIMARY KEY  CLUSTERED 
--	(
--		[prcObjId]
--	)  ON [PRIMARY] 
--GO

--ALTER TABLE [PrcPrcExtProp] WITH NOCHECK ADD 
--	 PRIMARY KEY  CLUSTERED 
--	(
--		[prcObjId],
--		[prcSeq]
--	)  ON [PRIMARY] 
--GO

--ALTER TABLE [PrcPrcInst] WITH NOCHECK ADD 
--	 PRIMARY KEY  CLUSTERED 
--	(
--		[prcObjId]
--	)  ON [PRIMARY] 
--GO

--ALTER TABLE [PrcPrcInscharProp] WITH NOCHECK ADD 
--	 PRIMARY KEY  CLUSTERED 
--	(
--		[prcObjId],
--		[prcSeq]
--	)  ON [PRIMARY] 
--GO

--ALTER TABLE [TskTask] WITH NOCHECK ADD 
--	 PRIMARY KEY  CLUSTERED 
--	(
--		[tskObjId]
--	)  ON [PRIMARY] 
--GO

--ALTER TABLE [TskTaskDef] WITH NOCHECK ADD 
--	 PRIMARY KEY  CLUSTERED 
--	(
--		[tskObjId]
--	)  ON [PRIMARY] 
--GO

--ALTER TABLE [TskTaskDefExtProp] WITH NOCHECK ADD 
--	 PRIMARY KEY  CLUSTERED 
--	(
--		[tskObjId],
--		[tskSeq]
--	)  ON [PRIMARY] 
--GO

--ALTER TABLE [TskTaskExtProp] WITH NOCHECK ADD 
--	 PRIMARY KEY  CLUSTERED 
--	(
--		[tskObjId],
--		[tskSeq]
--	)  ON [PRIMARY] 
--GO

-- CREATE  INDEX [IxAprApr2] ON [dbo].[AprApr]([aprStatus]) ON [PRIMARY]
--GO
--
-- CREATE  INDEX [IxAprApr] ON [dbo].[AprApr]([aprCreateUser]) ON [PRIMARY]
--GO
--
-- CREATE  INDEX [IxAprApr4] ON [dbo].[AprApr]([aprApprover]) ON [PRIMARY]
--GO
--
-- CREATE  INDEX [IxAprApr3] ON [dbo].[AprApr]([aprType]) ON [PRIMARY]
--GO
--
-- CREATE  INDEX [IxAprApr1] ON [dbo].[AprApr]([aprModifyUser]) ON [PRIMARY]
--GO
--
-- CREATE  INDEX [IxAprAprExtProp] ON [dbo].[AprAprExtProp]([aprName]) ON [PRIMARY]
--GO
--
-- CREATE  INDEX [IxAprAprExtProp2] ON [dbo].[AprAprExtProp]([aprValue]) ON [PRIMARY]
--GO
--
-- CREATE  INDEX [IxAprAprLine3] ON [dbo].[AprAprLine]([aprCorr]) ON [PRIMARY]
--GO
--
-- CREATE  INDEX [IxAprAprLine1] ON [dbo].[AprAprLine]([aprModifyUser]) ON [PRIMARY]
--GO
--
-- CREATE  INDEX [IxAprAprLine2] ON [dbo].[AprAprLine]([aprStatus]) ON [PRIMARY]
--GO
--
-- CREATE  INDEX [IxAprAprLine] ON [dbo].[AprAprLine]([aprCreateUser]) ON [PRIMARY]
--GO
--
-- CREATE  INDEX [IxAprAprLineExtProp2] ON [dbo].[AprAprLineExtProp]([aprValue]) ON [PRIMARY]
--GO
--
-- CREATE  INDEX [IxAprAprLineExtProp] ON [dbo].[AprAprLineExtProp]([aprName]) ON [PRIMARY]
--GO
--
-- CREATE  INDEX [IxChtChart5] ON [ChtChart]([chtModifyDate]) ON [PRIMARY]
--GO
--
-- CREATE  INDEX [IxChtChart3] ON [ChtChart]([chtCreateDate]) ON [PRIMARY]
--GO
--
-- CREATE  INDEX [IxChtChart] ON [ChtChart]([chtName]) ON [PRIMARY]
--GO
--
-- CREATE  INDEX [IxChtChart4] ON [ChtChart]([chtModifyUser]) ON [PRIMARY]
--GO
--
-- CREATE  INDEX [IxChtChart2] ON [ChtChart]([chtCreateUser]) ON [PRIMARY]
--GO
--
-- CREATE  INDEX [IxChtCharcharProp2] ON [ChtCharcharProp]([chtValue]) ON [PRIMARY]
--GO
--
-- CREATE  INDEX [IxChtCharcharProp] ON [ChtCharcharProp]([chtName]) ON [PRIMARY]
--GO
--
-- CREATE  INDEX [IxLnkList] ON [LnkList]([lnkCreateUser]) ON [PRIMARY]
--GO
--
-- CREATE  INDEX [IxLnkList1] ON [LnkList]([lnkModifyUser]) ON [PRIMARY]
--GO
--
-- CREATE  INDEX [IxLnkList2] ON [LnkList]([lnkType], [lnkCorr]) ON [PRIMARY]
--GO
--
-- CREATE  INDEX [IxLnkLischarProp1] ON [LnkLischarProp]([tskValue]) ON [PRIMARY]
--GO
--
-- CREATE  INDEX [IxLnkLischarProp] ON [LnkLischarProp]([tskName]) ON [PRIMARY]
--GO
--
-- CREATE  INDEX [IxLnkListItem1] ON [LnkListItem]([lnkRef]) ON [PRIMARY]
--GO
--
-- CREATE  INDEX [IxLnkListItem] ON [LnkListItem]([lnkType]) ON [PRIMARY]
--GO
--
-- CREATE  INDEX [IxLnkLnk2] ON [LnkLnk]([lnkCorr]) ON [PRIMARY]
--GO
--
-- CREATE  INDEX [IxLnkLnk6] ON [LnkLnk]([lnkToLabel]) ON [PRIMARY]
--GO
--
-- CREATE  INDEX [IxLnkLnk4] ON [LnkLnk]([lnkFromLabel]) ON [PRIMARY]
--GO
--
-- CREATE  INDEX [IxLnkLnk] ON [LnkLnk]([lnkCreateUser]) ON [PRIMARY]
--GO
--
-- CREATE  INDEX [IxLnkLnk1] ON [LnkLnk]([lnkModifyUser]) ON [PRIMARY]
--GO
--
-- CREATE  INDEX [IxLnkLnk3] ON [LnkLnk]([lnkFromType], [lnkFromRef]) ON [PRIMARY]
--GO
--
-- CREATE  INDEX [IxLnkLnk7] ON [LnkLnk]([lnkCondType]) ON [PRIMARY]
--GO
--
-- CREATE  INDEX [IxLnkLnk5] ON [LnkLnk]([lnkToType], [lnkToRef]) ON [PRIMARY]
--GO
--
-- CREATE  INDEX [IxLnkLnkExtProp] ON [LnkLnkExtProp]([tskName]) ON [PRIMARY]
--GO
--
-- CREATE  INDEX [IxLnkLnkExtProp1] ON [LnkLnkExtProp]([tskValue]) ON [PRIMARY]
--GO
--
-- CREATE  INDEX [IxLnkMap3] ON [LnkMap]([lnkFromType], [lnkFromRef]) ON [PRIMARY]
--GO
--
-- CREATE  INDEX [IxLnkMap] ON [LnkMap]([lnkCreateUser]) ON [PRIMARY]
--GO
--
-- CREATE  INDEX [IxLnkMap2] ON [LnkMap]([lnkType]) ON [PRIMARY]
--GO
--
-- CREATE  INDEX [IxLnkMap1] ON [LnkMap]([lnkModifyUser]) ON [PRIMARY]
--GO
--
-- CREATE  INDEX [IxLnkMap4] ON [LnkMap]([lnkToType], [lnkToRef]) ON [PRIMARY]
--GO
--
-- CREATE  INDEX [IxLnkMapExtProp] ON [LnkMapExtProp]([tskName]) ON [PRIMARY]
--GO
--
-- CREATE  INDEX [IxLnkMapExtProp1] ON [LnkMapExtProp]([tskValue]) ON [PRIMARY]
--GO
--
-- CREATE  INDEX [IxLnkValue2] ON [LnkValue]([lnkType], [lnkRef]) ON [PRIMARY]
--GO
--
-- CREATE  INDEX [IxLnkValue] ON [LnkValue]([lnkCreateUser]) ON [PRIMARY]
--GO
--
-- CREATE  INDEX [IxLnkValue3] ON [LnkValue]([lnkExpDate]) ON [PRIMARY]
--GO
--
-- CREATE  INDEX [IxLnkValue1] ON [LnkValue]([lnkModifyUser]) ON [PRIMARY]
--GO
--
-- CREATE  INDEX [IxLnkValue4] ON [LnkValue]([lnkStatus]) ON [PRIMARY]
--GO
--
-- CREATE  INDEX [IxLnkValueExtProp] ON [LnkValueExtProp]([tskName]) ON [PRIMARY]
--GO
--
-- CREATE  INDEX [IxLnkValueExtProp1] ON [LnkValueExtProp]([tskValue]) ON [PRIMARY]
--GO
--
-- CREATE  INDEX [IxMdlChart2] ON [MdlModel]([mdlModifyUser]) ON [PRIMARY]
--GO
--
-- CREATE  INDEX [IxMdlChart1] ON [MdlModel]([mdlCreateUser]) ON [PRIMARY]
--GO
--
-- CREATE  INDEX [IxMdlChart] ON [MdlModel]([mdlName]) ON [PRIMARY]
--GO
--
-- CREATE  INDEX [IxMdlModelExtProp] ON [MdlModelExtProp]([mdlName]) ON [PRIMARY]
--GO
--
-- CREATE  INDEX [IxMdlModelExtProp1] ON [MdlModelExtProp]([mdlValue]) ON [PRIMARY]
--GO
--
-- CREATE  INDEX [IxPrcPrc4] ON [PrcPrc]([prcPrcId]) ON [PRIMARY]
--GO
--
-- CREATE  INDEX [IxPrcPrc] ON [PrcPrc]([prcCreateUser]) ON [PRIMARY]
--GO
--
-- CREATE  INDEX [IxPrcPrc2] ON [PrcPrc]([prcDId]) ON [PRIMARY]
--GO
--
-- CREATE  INDEX [IxPrcPrc3] ON [PrcPrc]([prcDVer]) ON [PRIMARY]
--GO
--
-- CREATE  INDEX [IxPrcPrc1] ON [PrcPrc]([prcModifyUser]) ON [PRIMARY]
--GO
--
-- CREATE  INDEX [IxPrcPrcExtProp2] ON [PrcPrcExtProp]([prcValue]) ON [PRIMARY]
--GO
--
-- CREATE  INDEX [IxPrcPrcExtProp] ON [PrcPrcExtProp]([prcName]) ON [PRIMARY]
--GO
--
-- CREATE  INDEX [IxPrcPrcInst1] ON [PrcPrcInst]([prcModifyUser]) ON [PRIMARY]
--GO
--
-- CREATE  INDEX [IxPrcPrcInst2] ON [PrcPrcInst]([prcStatus]) ON [PRIMARY]
--GO
--
-- CREATE  INDEX [IxPrcPrcInst4] ON [PrcPrcInst]([prcDId]) ON [PRIMARY]
--GO
--
-- CREATE  INDEX [IxPrcPrcInst3] ON [PrcPrcInst]([prcTitle]) ON [PRIMARY]
--GO
--
-- CREATE  INDEX [IxPrcPrcInst] ON [PrcPrcInst]([prcCreateUser]) ON [PRIMARY]
--GO
--
-- CREATE  INDEX [IxPrcPrcInst6] ON [PrcPrcInst]([prcPrcId]) ON [PRIMARY]
--GO
--
-- CREATE  INDEX [IxPrcPrcInst5] ON [PrcPrcInst]([prcDVer]) ON [PRIMARY]
--GO
--
-- CREATE  INDEX [IxPrcPrcInscharProp] ON [PrcPrcInscharProp]([prcName]) ON [PRIMARY]
--GO
--
-- CREATE  INDEX [IxPrcPrcInscharProp2] ON [PrcPrcInscharProp]([prcValue]) ON [PRIMARY]
--GO
--
-- CREATE  INDEX [IxTskTask10] ON [TskTask]([tskAssignee]) ON [PRIMARY]
--GO
--
-- CREATE  INDEX [IxTskTask15] ON [TskTask]([tskDef]) ON [PRIMARY]
--GO
--
-- CREATE  INDEX [IxTskTask7] ON [TskTask]([tskType]) ON [PRIMARY]
--GO
--
-- CREATE  INDEX [IxTskTask4] ON [TskTask]([tskModifyDate]) ON [PRIMARY]
--GO
--
-- CREATE  INDEX [IxTskTask11] ON [TskTask]([tskPerformer]) ON [PRIMARY]
--GO
--
-- CREATE  INDEX [IxTskTask16] ON [TskTask]([tskForm]) ON [PRIMARY]
--GO
--
-- CREATE  INDEX [IxTskTask8] ON [TskTask]([tskPrcInstId]) ON [PRIMARY]
--GO
--
-- CREATE  INDEX [IxTskTask2] ON [TskTask]([tskCreateDate]) ON [PRIMARY]
--GO
--
-- CREATE  INDEX [IxTskTask12] ON [TskTask]([tskStartDate]) ON [PRIMARY]
--GO
--
-- CREATE  INDEX [IxTskTask5] ON [TskTask]([tskStatus]) ON [PRIMARY]
--GO
--
-- CREATE  INDEX [IxTskTask9] ON [TskTask]([tskAssigner]) ON [PRIMARY]
--GO
--
-- CREATE  INDEX [IxTskTask17] ON [TskTask]([tskMultiInstId]) ON [PRIMARY]
--GO
--
-- CREATE  INDEX [IxTskTask1] ON [TskTask]([tskCreateUser]) ON [PRIMARY]
--GO
--
-- CREATE  INDEX [IxTskTask13] ON [TskTask]([tskAssignDate]) ON [PRIMARY]
--GO
--
-- CREATE  INDEX [IxTskTask] ON [TskTask]([tskName]) ON [PRIMARY]
--GO
--
-- CREATE  INDEX [IxTskTask14] ON [TskTask]([tskExecuteDate]) ON [PRIMARY]
--GO
--
-- CREATE  INDEX [IxTskTask3] ON [TskTask]([tskModifyUser]) ON [PRIMARY]
--GO
--
-- CREATE  INDEX [IxTskTask6] ON [TskTask]([tskCorr]) ON [PRIMARY]
--GO
--
-- CREATE  INDEX [IxTskTaskDef6] ON [TskTaskDef]([tskType]) ON [PRIMARY]
--GO
--
-- CREATE  INDEX [IxTskTaskDef] ON [TskTaskDef]([tskName]) ON [PRIMARY]
--GO
--
-- CREATE  INDEX [IxTskTaskDef2] ON [TskTaskDef]([tskCreateDate]) ON [PRIMARY]
--GO
--
-- CREATE  INDEX [IxTskTaskDef5] ON [TskTaskDef]([tskStatus]) ON [PRIMARY]
--GO
--
-- CREATE  INDEX [IxTskTaskDef3] ON [TskTaskDef]([tskModifyUser]) ON [PRIMARY]
--GO
--
-- CREATE  INDEX [IxTskTaskDef1] ON [TskTaskDef]([tskCreateUser]) ON [PRIMARY]
--GO
--
-- CREATE  INDEX [IxTskTaskDef7] ON [TskTaskDef]([tskPriority]) ON [PRIMARY]
--GO
--
-- CREATE  INDEX [IxTskTask8] ON [TskTaskDef]([tskForm]) ON [PRIMARY]
--GO
--
-- CREATE  INDEX [IxTskTaskDef4] ON [TskTaskDef]([tskModifyDate]) ON [PRIMARY]
--GO
--
-- CREATE  INDEX [IxTskTaskDefExtProp2] ON [TskTaskDefExtProp]([tskValue]) ON [PRIMARY]
--GO
--
-- CREATE  INDEX [IxTskTaskDefExtProp] ON [TskTaskDefExtProp]([tskName]) ON [PRIMARY]
--GO
--
-- CREATE  INDEX [IxTskTaskExtProp2] ON [TskTaskExtProp]([tskValue]) ON [PRIMARY]
--GO
--
-- CREATE  INDEX [IxTskTaskExtProp] ON [TskTaskExtProp]([tskName]) ON [PRIMARY]
--GO
--
--ALTER TABLE [dbo].[AprApr] ADD 
--	CONSTRAINT [FkAprAprLineAprs] FOREIGN KEY 
--	(
--		[approvalLine]
--	) REFERENCES [dbo].[AprAprLine] (
--		[aprObjId]
--	)
--GO
--
--ALTER TABLE [dbo].[AprAprExtProp] ADD 
--	CONSTRAINT [FkAprAprExtProp] FOREIGN KEY 
--	(
--		[aprObjId]
--	) REFERENCES [dbo].[AprApr] (
--		[aprObjId]
--	)
--GO
--
--ALTER TABLE [dbo].[AprAprLineExtProp] ADD 
--	CONSTRAINT [FkAprAprLineExtProp] FOREIGN KEY 
--	(
--		[aprObjId]
--	) REFERENCES [dbo].[AprAprLine] (
--		[aprObjId]
--	)
--GO
--
--ALTER TABLE [ChtCharcharProp] ADD 
--	CONSTRAINT [FkChtCharcharProp] FOREIGN KEY 
--	(
--		[chtObjId]
--	) REFERENCES [ChtChart] (
--		[chtObjId]
--	)
--GO
--
--ALTER TABLE [LnkLischarProp] ADD 
--	CONSTRAINT [FkLnkLischarProp] FOREIGN KEY 
--	(
--		[lnkObjId]
--	) REFERENCES [LnkList] (
--		[lnkObjId]
--	)
--GO
--
--ALTER TABLE [LnkListItem] ADD 
--	CONSTRAINT [FkLnkListItem] FOREIGN KEY 
--	(
--		[lnkObjId]
--	) REFERENCES [LnkList] (
--		[lnkObjId]
--	)
--GO
--
--ALTER TABLE [LnkLnkExtProp] ADD 
--	CONSTRAINT [FkLnkLnkExtProp] FOREIGN KEY 
--	(
--		[lnkObjId]
--	) REFERENCES [LnkLnk] (
--		[lnkObjId]
--	)
--GO
--
--ALTER TABLE [LnkMapExtProp] ADD 
--	CONSTRAINT [FkLnkMapExtProp] FOREIGN KEY 
--	(
--		[lnkObjId]
--	) REFERENCES [LnkMap] (
--		[lnkObjId]
--	)
--GO
--
--ALTER TABLE [LnkValueExtProp] ADD 
--	CONSTRAINT [FkLnkValueExtProp] FOREIGN KEY 
--	(
--		[lnkObjId]
--	) REFERENCES [LnkValue] (
--		[lnkObjId]
--	)
--GO
--
--ALTER TABLE [MdlModelExtProp] ADD 
--	CONSTRAINT [FkMdlModelExtProp] FOREIGN KEY 
--	(
--		[mdlObjId]
--	) REFERENCES [MdlModel] (
--		[mdlObjId]
--	)
--GO
--
--ALTER TABLE [PrcPrcExtProp] ADD 
--	CONSTRAINT [FkPrcPrcExtProp] FOREIGN KEY 
--	(
--		[prcObjId]
--	) REFERENCES [PrcPrc] (
--		[prcObjId]
--	)
--GO
--
--ALTER TABLE [PrcPrcInscharProp] ADD 
--	CONSTRAINT [FkPrcPrcInscharProp] FOREIGN KEY 
--	(
--		[prcObjId]
--	) REFERENCES [PrcPrcInst] (
--		[prcObjId]
--	)
--GO
--
--ALTER TABLE [TskTaskDefExtProp] ADD 
--	CONSTRAINT [FkTskTaskDefExtProp] FOREIGN KEY 
--	(
--		[tskObjId]
--	) REFERENCES [TskTaskDef] (
--		[tskObjId]
--	)
--GO
--
--ALTER TABLE [TskTaskExtProp] ADD 
--	CONSTRAINT [FkTskTaskExtProp] FOREIGN KEY 
--	(
--		[tskObjId]
--	) REFERENCES [TskTask] (
--		[tskObjId]
--	)
--GO

-- 3.0 추가


-- 커뮤니티 그룹
CREATE TABLE SWOrgGroup (
	id	varchar(50) not null,
	companyId	varchar(50),
	name	varchar(100),
	groupLeader	varchar(50),
	groupType	varchar(1),
	status	varchar(1),
	picture varchar(100),
	description	varchar(4000),
	maxMember int,
	autoApproval Number(3),
	creator	varchar(50),
	createdTime	timestamp,
	modifier	varchar(50),
	modifiedTime	timestamp,
	primary key (id)
);

-- 커뮤니티 그룹 멤버
CREATE TABLE SWOrgGroupMember (
	groupId	varchar(50) not null,
	userId	varchar(50) not null,
	joinType	varchar(1),
	joinStatus	varchar(1),
	joinTime	timestamp,
	outTime 	timestamp,
	memberSeq	int,
	creator varchar(50),
	createdtime timestamp ,
	modifier varchar(50),
	modifiedtime timestamp ,
	primary key (groupId, userId)
);

-- 폴더
CREATE TABLE SwFolder (
	id varchar(50) NOT NULL,
	companyid varchar(100),
	parentid varchar(50),
	name varchar(255),
	disporder int,
	description varchar(4000),
	creator varchar(30),
	createdtime timestamp,
	modifier varchar(30),
	modifiedtime timestamp,
	tskWorkspaceId varchar(50),
	tskRefType varchar(50),
	primary key (id)
);

-- 폴더파일
CREATE TABLE SwFolderFile (
	folderId varchar(50) NOT NULL,
	fileId varchar(50) NOT NULL,
	fileSeq int,
  	primary key(folderId, fileId)
);

-- 좋아요
CREATE TABLE SwLike (
	id varchar(50) NOT NULL,
	refType int,
	refId varchar(50),
	creator varchar(50),
	createdTime timestamp,
	primary key (id)
);

-- 메세지
CREATE TABLE SWMessage(
	id varchar(50) NOT NULL,
	content clob,
	sendUserId varchar(50),
	targetUserId varchar(50),
	deleteUserId varchar(50),
	isChecked Number(3),
	chatId varchar(50),
	chattersId varchar(2000),
	checkedTime timestamp,
	creator varchar (50),
	createdTime timestamp,
	modifier varchar (50),
	modifiedTime timestamp,
	primary key (id)
);


-- 로그인 사용자
CREATE TABLE SwLoginUser (
	userId varchar(50) NOT NULL,
	loginTime timestamp,
	primary key (userId)
);

--메일관련 테이블 Start

-- create sequence autonum 
--     increment by 1  
--     start with 1       
--     nomaxvalue    
--     nocycle             
--     nocache;

CREATE TABLE folder_db_objects (
    id Number NOT NULL,
    username varchar(255) NOT NULL,
    parent_id Number NOT NULL,
    folder_name varchar(100) NOT NULL,
    folder_type int NOT NULL,
    primary key (id)
);

CREATE TABLE msg_db_objects (
    id Number NOT NULL,
    uid_ varchar(100),
    username varchar(255) NOT NULL,
    folder_id Number NOT NULL,
    unique_id varchar(100) NOT NULL,
    sender varchar(255),
    receiver clob,
    cc clob,
    bcc clob,
    replyTo varchar(255),
    subject varchar(255),
    multipart smallint,
    priority int,
    sentdate timestamp,
    unread smallint NOT NULL,
    msg_size Number NOT NULL,
    email blob NOT NULL,
    primary key (id)
);

CREATE TABLE msg_db_uids (
    username varchar(255) NOT NULL,
    uid_ varchar(100) NOT NULL
);


CREATE TABLE msg_rules (
    id Number NOT NULL,
    username varchar(255) NOT NULL,
    portion varchar(100) NOT NULL,
    rule_condition varchar(30) NOT NULL,
    keyword varchar(255) NOT NULL,
    rule_action varchar(30) NOT NULL,
    destination varchar(100) NOT NULL,
    primary key (id)
);
--메일관련 테이블 End

--알림 카운트를 세고 알림 조회를 위한 테이블
CREATE TABLE SWPublishNotice (
	id varchar(50) NOT NULL,
	type int,
	refType varchar(50),
	refId varchar(100),
	assignee varchar(50),
	creator varchar (50),
	createdTime timestamp,
	modifier varchar (50),
	modifiedTime timestamp,
	primary key (id)
);


-- SwMailServer (메일서버정보)
CREATE TABLE SwMailServer (
	id varchar(50) NOT NULL,
	name varchar(100) NOT NULL,
	companyId varchar(50) NOT NULL,
	fetchServer varchar(50),
	fetchServerPort int,
	fetchProtocol varchar(10),
	fetchSsl Number(3),
	smtpServer varchar(50),
	smtpServerPort int,
	smtpAuthenticated Number(3),
	smtpSsl Number(3),
	pwchangeapi varchar(300),
	pwchangedefaultdata varchar(100),
	pwchangeparamid varchar(50),
	pwchangeparamoldpw varchar(50),
	pwchangeparamnewpw varchar(50),
	creator	varchar(50),
	createdtime timestamp,
	modifier varchar(50),
	modifiedtime timestamp,
    primary key (id)
);

-- SwMailAccount (사용자메일계정)
CREATE TABLE SwMailAccount (
	id varchar(50) NOT NULL,
	userId varchar(50) NOT NULL,
	mailServerId varchar(50) NOT NULL,
	mailServerName varchar(100) NOT NULL,
	mailId varchar(50) NOT NULL,
	mailUserName varchar(50),
	mailPassword varchar(50) NOT NULL,
	mailSignature varchar(4000),
	useMailSign Number(3),
	senderUserTitle varchar(50),
	mailDeleteFetched varchar(10),
	junks clob,
	creator	varchar(50),
	createdtime timestamp,
	modifier varchar(50),
	modifiedtime timestamp,
    primary key (id)
);

-- 아이디 자동생성
CREATE TABLE SwAutoIndexDef (
	objId varchar(50) NOT NULL,
	formId varchar(100) NOT NULL,
	version int,
	fieldId varchar(10) NOT NULL,	
	creator	varchar(50),
	createdtime timestamp,
	modifier varchar(50),
	modifiedtime timestamp,
    primary key (objId)
);

-- 오라클 예약어 인듯(increment)
--CREATE TABLE SwAutoIndexRuls (
--	objId varchar(50) NOT NULL,
--	ruleId varchar(100),
--	type varchar(50),
--	codeValue varchar(100),
--	seperator varchar(10),
--	increment int,
--	incrementBy varchar(50),
--	digits varchar(10),
--	items varchar(500),
--	indexSeq int NOT NULL,	
--    primary key (objId, indexSeq)
--);
CREATE TABLE SwAutoIndexRuls (
	objId varchar(50) NOT NULL,
	ruleId varchar(100),
	type varchar(50),
	codeValue varchar(100),
	seperator varchar(10),
	increments int,
	incrementBy varchar(50),
	digits varchar(10),
	items varchar(500),
	indexSeq int NOT NULL,	
    primary key (objId, indexSeq)
);

CREATE TABLE SwAutoIndexInst (
	objId varchar(50) NOT NULL,
	instanceId varchar(100),
	formId varchar(100),
	fieldId varchar(10),
	refType varchar(10),
	idType varchar(50),
	idValue varchar(200),
	seq int,
	creator	varchar(50),
	createdtime timestamp,
	modifier varchar(50),
	modifiedtime timestamp,
    primary key (objId)
);
--부서 권한
CREATE TABLE SwAuthDepartment (
	id varchar(50) NOT NULL,
	deptId varchar(100),
	deptAuthType varchar(50),
	roleKey varchar(50),
	customUser varchar(4000),
    primary key (id)
);
--그룹 권한
CREATE TABLE SwAuthGroup (
	id varchar(50) NOT NULL,
	groupId varchar(100),
	groupAuthType varchar(50),
	roleKey varchar(50),
	customUser varchar(4000),
    primary key (id)
);
CREATE TABLE SWFileDownHistory (
	id varchar(50) NOT NULL,
	fileId varchar(50),
	fileName varchar(255),
	downloadUserId varchar(50),
	refPackageId varchar(100),
	refPackageName varchar(255),
	refPrcInstId varchar(100),
	refPrcInstName varchar(200),
	refTaskId varchar(50),
	refTaskName varchar(200),
	creator	varchar(50),
	createdtime timestamp,
	modifier varchar(50),
	modifiedtime timestamp,
    primary key (id)
);

CREATE TABLE SwLoginUserHistory (
	id varchar(50) NOT NULL,
	userId varchar(50),
	loginTime timestamp,
    primary key (id)
);


CREATE TABLE SWScheduleDef (
	objId varchar(50) NOT NULL,
	targetPackageType varchar(10),
	targetPackageId varchar(100),
	targetFormId varchar(100),
	targetFieldId varchar(100),
	status varchar(10),
	name varchar(200),
	groupName varchar(100),
	jobName varchar(100),
	triggerName varchar(100),
	targetClass varchar(200),
	cronExpression varchar(50),
	isAutoStart varchar(10),
	creator	varchar(50),
	createdtime timestamp,
	modifier varchar(50),
	modifiedtime timestamp,
    primary key (objId)
);

CREATE TABLE SWMessageNotice (
	id varchar(50) NOT NULL,
	type varchar(50),
	workId varchar(50),
	workSpaceType varchar(50),
	workSpaceId varchar(50),
	refType varchar(50),
	refId varchar(100),
	taskId varchar(100),
	assignee varchar(50),
	creator varchar (50),
	createdTime timestamp,
	modifier varchar (50),
	modifiedTime timestamp,
	primary key (id)
);

CREATE TABLE SWAlarmNotice (
	id varchar(50) NOT NULL,
	noticeTime timestamp,
	workId varchar(50),
	recordId varchar(100),
	targetUser varchar(50),
	companyId varchar(50),
	creator varchar (50),
	createdTime timestamp,
	modifier varchar (50),
	modifiedTime timestamp,
	primary key (id)
);



CREATE TABLE SWAuthProxy (
	id varchar(50) NOT NULL,
	resourceId varchar(50),
	type varchar(20),
	accessLevel varchar(50),
	accessValue varchar(4000),
	primary key (id)
);


CREATE TABLE SwReport (
	id varchar(50) NOT NULL,
	name varchar(100),
	type int,
	targetWorkId varchar(50),
	targetWorkType int,
	dataSourceType int,
	externalServiceId varchar(50),
	reportTableKey varchar(50),
	searchFilterId varchar(50),
	owner varchar(50),
	chartType int,
	
	xAxis varchar(50),
	xAxisSelector varchar(50),
	xAxisSort varchar(50),
	xAxisMaxRecords int,
	xSecondAxis varchar(50),
	xSecondAxisSelector varchar(50),
	xSecondAxisSort varchar(50),
	yAxis varchar(50),
	yAxisSelector varchar(50),
	valueType varchar(50),
	zAxis varchar(50),
	zAxisSelector varchar(50),
	zAxisSort varchar(50),
	zSecondAxis varchar(50),
	zSecondAxisSelector varchar(50),
	zSecondAxisSort varchar(50),
	workspaceid varchar(100),
	workspacetype varchar(50),
	accesslevel varchar(50),
	accessvalue varchar(4000),
	creator	varchar(50),
	createdtime timestamp,
	modifier varchar(50),
	modifiedtime timestamp,
    primary key (id)
);


CREATE TABLE SwReportPane (
	id varchar(50) NOT NULL,
	name varchar(100),
	columnSpans int,
	position int,
	reportId varchar(50),
	reportName varchar(100),
	reportType int,
	targetWorkId varchar(50),
	chartType int,
	isChartView int,
	isStacked int,
	showLegend int,
	stringLabelRotation varchar(50),
	owner varchar(50),
	creator	varchar(50),
	createdtime timestamp,
	modifier varchar(50),
	modifiedtime timestamp,
    primary key (id)
);




