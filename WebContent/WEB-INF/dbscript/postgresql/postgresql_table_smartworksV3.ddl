-- 회사 : 단위 + 시스템
CREATE TABLE sworgcompany (
	id character varying(50) NOT NULL,
	name character varying(50),
	address character varying(4000),
	domainid character varying(50),
	workitemid character varying(50),
	creator	character varying(50),
	createdtime timestamp,
	modifier	character varying(50),
	modifiedtime timestamp,
	primary key (id)	
);

-- 사용자 : 단위 + 시스템
CREATE TABLE sworguser (
	id character varying(50) NOT NULL,
	companyid character varying(50),
	deptid character varying(50),
	adjunctDeptIds character varying(500),
	roleid character varying(50),
	authid character varying(50),
	empno character varying(50),
	name character varying(255),
	nickName character varying(255),
	type character varying(50),
	pos character varying(50),
	email character varying(50),
	useMail bool,
	passwd character varying(50),
	lang character varying(20),
	stdtime character varying(20),
	picture character varying(50),
	sign character varying(50),
	useSign bool,
	lunarBirthDay bool,
	domainid character varying(50),
	workitemid character varying(50),
	creator	character varying(50),
	createdtime timestamp,
	modifier	character varying(50),
	modifiedtime timestamp,
	retiree character varying(50),
	mobileNo character varying(50),
	internalNo character varying(50),
	locale character varying(20),
	timeZone character varying(20),
	primary key (id)
);

-- 부서 : 단위 + 시스템
CREATE TABLE sworgdept (
	id character varying(50) NOT NULL,
	companyid character varying(50),
	parentid character varying(50),
	type character varying(50),
	name character varying(50),
	description character varying(4000),
	domainid character varying(50),
	workitemid character varying(50),
	creator	character varying(50),
	createdtime timestamp,
	modifier	character varying(50),
	modifiedtime timestamp,
	picture character varying(50),
	workspaceid character varying(100),
	workspacetype character varying(50),
	accesslevel character varying(50),
	accessvalue character varying(4000),
	hits integer,
	primary key (id)
);

-- 역할 : 단위 + 시스템
CREATE TABLE sworgrole (
	id character varying(50) NOT NULL,
	companyid character varying(50),
	name character varying(50),
	description character varying(4000),
	domainid character varying(50),
	workitemid character varying(50),
	creator	character varying(50),
	createdtime timestamp,
	modifier character varying(50),
	modifiedtime timestamp,
	primary key (id)
);

---- 일정 : 단위 + 시스템
--CREATE TABLE swschedule (
--	id 	character varying(50) NOT NULL,
--	userid character varying(50) NOT NULL,
--	title character varying(255),
--	startdate timestamp without time zone,
--	enddate timestamp without time zone,
--	type character varying(50),
--	place character varying(255),
--	fromtime timestamp without time zone,
--	totime timestamp without time zone,
--	referencer character varying(1000),
--	content character varying(4000),
--	alerttype character varying(50),
--	accesslevel character varying(20),
--	domainid character varying(50),
--	workitemid character varying(50),
--	creator	character varying(50),
--	createdtime timestamp without time zone,
--	modifier	character varying(50),
--	modifiedtime timestamp without time zone,
--	primary key (id)
--);

---- 부재 설정
--CREATE TABLE swabsentee (
--	id character varying(50) NOT NULL,
--	absentee character varying(30),
--	startdate timestamp without time zone,
--	enddate timestamp without time zone,
--	registeredtime timestamp without time zone,
--	entrustyn char(1),
--	entruster character varying(30),
--	reason character varying(4000),
--	primary key (id)
--);

---- 코드
--CREATE TABLE swcode (
--	id character varying(50) NOT NULL,
--	categoryid character varying(50),
--	code character varying(50),
--	name character varying(255),
--	disporder int,
--	description character varying(4000),
--	primary key (id)
--);

-- 리소스
CREATE TABLE swcategory (
	id character varying(50) NOT NULL,
	companyid character varying(100),
	parentid character varying(50),
	name character varying(255),
	disporder int,
	description character varying(4000),
	creator character varying(30),
	createdtime timestamp,
	modifier character varying(30),
	modifiedtime timestamp,	
	primary key (id)
);

--CREATE TABLE swcategoryextprop(
--	id character varying(50) NOT NULL,
--	ctgname character varying(100) NULL,
--	ctgvalue character varying(100) NULL,
--	ctgseq int NOT NULL,
--	primary key (id, ctgseq)
--);

CREATE TABLE swpackage (
	id character varying(50) NOT NULL,
	categoryid character varying(50),
	packageid character varying(50),
	version int,
	name character varying(100),
	type character varying(20),
	status character varying(30),
	latestdeployedyn char(1),
	helpUrl character varying(500),
	manualFileName character varying(100),
	creator character varying(30),
	createdtime timestamp,
	modifier character varying(30),
	modifiedtime timestamp,
	description character varying(4000),
	primary key (id)
);

CREATE TABLE swprocess (
	id character varying(50) NOT NULL,
	packageid character varying(50),
	processid character varying(50),
	version int,
	name character varying(100),
	status character varying(30),
	publishmode character varying(10),
	keyword character varying(255),	
	creator character varying(30),
	createdtime timestamp,
	modifier character varying(30),
	modifiedtime timestamp,
	ownerdept character varying(30),
	owner character varying(30),
	encoding character varying(15),
	description character varying(4000),
	content text,
	primary key (id)
);

CREATE TABLE swform (
	id character varying(50) NOT NULL,
	packageid character varying(50),
	formid character varying(50),
	version int,
	name character varying(100),
	type character varying(20),
	status character varying(30),
	publishmode character varying(10),
	keyword character varying(255),
	creator character varying(30),
	createdtime timestamp,
	modifier character varying(30),
	modifiedtime timestamp,
	ownerdept character varying(30),
	owner character varying(30),
	encoding character varying(15),
	description character varying(4000),
	content text,
	primary key (id)
);

CREATE TABLE swworktype (
	id character varying(50) NOT NULL,
	name character varying(255),
	formuid character varying(50),
	stepcount int,
	type character varying(30),
	duration int,
	primary key (id)
);

--CREATE TABLE swworktypeperformerline (
--	worktypeid character varying(50) NOT NULL,
--	step int,
--	performertype int,
--	performer character varying(30),
--	primary key (worktypeid, step)
--);

-- 의견
CREATE TABLE swopinion (
	id character varying(50) NOT NULL,
	reftype int,
	groupid character varying(50),
	refid character varying(50),
	domainid character varying(400),
	formid character varying(400),
	title character varying(255),
	opinion character varying(4000),
	writer character varying(30),
	writtentime timestamp,
	modifier character varying(30),
	modifiedTime timestamp,
	primary key (id)
);

---- qna
--CREATE TABLE swqna (
--	id character varying(50) NOT NULL,
--	parentid character varying(50),
--	reftype int,
--	refid character varying(50),
--	writer character varying(30),
--	writtentime timestamp without time zone,
--	title character varying(255),
--	content character varying(4000),
--	primary key (id)
--);

-- tag : 구성 바꿔야 함... TO-D0 - taskObjId와 매핑될 수 있도록 구성 
CREATE TABLE swtag (
	id character varying(50) NOT NULL,
	userid character varying(30),
	tag character varying(50),
	createdtime timestamp,
	primary key (id)
);

--CREATE TABLE swpost (
--	id character varying(50) NOT NULL,
--	tagid character varying(50),
--	reftype int,
--	refgroupid character varying(50),
--	refid character varying(50),
--	typename character varying(255),
--	title character varying(4000),
--	createdtime timestamp without time zone,
--	primary key (id)
--);

---- 문서관리 : 단위 + 시스템
--CREATE TABLE swdocument (
--  	id character varying(50) NOT NULL,
--  	title character varying(50),
--  	code character varying(50),
--	ownerdept character varying(50),
--	owneruser character varying(50),
--  	filegroupid character varying(255),
--  	reftype int,
--  	refid character varying(50),
--	domainid character varying(50),
--	workitemid character varying(50),
--	creator	character varying(50),
--	createdtime timestamp without time zone,
--	modifier	character varying(50),
--	modifiedtime timestamp without time zone,
--  	primary key(id)
--);

CREATE TABLE swfile (
	id character varying(50) NOT NULL,	  
  	type character varying(10),	  
  	filename character varying(255),
  	filepath character varying(1000),  
	filesize int,
	writtentime timestamp,
	deleteAction bool,
  	primary key(id)
);

CREATE TABLE swdocgroup (
	tskInstanceId character varying(50) NOT NULL,
  	groupId character varying(50) NOT NULL,
  	docId character varying(50) NOT NULL,
  	primary key(groupId, docId)
);

---- 프로세스 엔진
--CREATE TABLE swactinstprop (
--	id character varying(50) NOT NULL,
--	name character varying(100) NOT NULL,
--	data character varying(2000),
--	primary key (id, name)
--);

--CREATE TABLE swactinst (
--	id character varying(50) NOT NULL,
--	prcinstid character varying(50),		
--	repeatcount int,
--	loopcount int,
--	exestep int,
--	uptodateyn char(1),
--	activityid character varying(50),
--	name character varying(50),		
--	status character varying(30),
--	type int,
--	apprefid character varying(50),
--	starttime timestamp without time zone,
--	endtime timestamp without time zone,
--	primary key (id)
--);

--CREATE TABLE swprcinst (
--	id character varying(50) NOT NULL,
--	processid character varying(50),
--	version int,
--	title character varying(255),
--	type int,
--	step int,
--	status character varying(30),
--	initiator character varying(50),
--	starttime timestamp without time zone,
--	endtime timestamp without time zone,
--	bizstatus character varying(50),
--	keyword character varying(100),
--	description character varying(4000),
--	primary key (id)
--);

--CREATE TABLE swprcinstprop (
--	id character varying(50) NOT NULL,
--	step int NOT NULL,
--	name character varying(100) NOT NULL,
--	data character varying(4000),
--	primary key (id, step)
--);

--CREATE TABLE swvarinst (
--	prcinstid character varying(50) NOT NULL,
--	refid character varying(50) NOT NULL,
--	name character varying(50) NOT NULL,
--	type int,
--	datatype character varying(50),
--	data text,
--	primary key (prcinstid, refid, name)
--);

--CREATE TABLE swruntimeerror (
--	id character varying(50) NOT NULL,
--	processid character varying(50),
--	version int,
--	prcinstid character varying(50),
--	actinstid character varying(50),
--	status int,
--	step int,
--	performer character varying(50),
--	reactor character varying(50),
--	source character varying(50),
--	type character varying(255),
--	trace character varying(2000),
--	occurredtime timestamp without time zone,
--	reactedtime timestamp without time zone,
--	message text,
--	primary key (id)
--);

---- 워크리스트
--CREATE TABLE swworkitem (
--	id character varying(50) NOT NULL,
--	worktypeid character varying(50),
--	title character varying(255),
--	status int,
--	assigner character varying(30),
--	assigneetype int,
--	assignee character varying(30),
--	performer character varying(30),
--	keyword character varying(255),
--	importance char(1),
--	priority char(1),
--	createdtime timestamp without time zone,
--	completedtime timestamp without time zone,
--	duetime timestamp without time zone,
--	previousid character varying(50),
--	type int,
--	step int,
--	refid character varying(50),
--	groupid character varying(50),
--	deletedyn char(1),
--	description character varying(4000),	
--	data text,
--	primary key (id)
--);

--CREATE TABLE swworkitementrust (
--	id character varying(50) NOT NULL,
--	workitemid character varying(50),
--	previousassignee character varying(30),
--	entruster character varying(30),
--	operator character varying(30),
--	entrustedtime timestamp without time zone,
--	primary key (id)
--);

--CREATE TABLE swworkitemparticipant (
--	id character varying(50) NOT NULL,
--	workitemid character varying(50),
--	participanttype int,
--	participant character varying(30),
--	primary key (id)
--);

--CREATE TABLE swworkitemprop (
--	workitemid character varying(50) NOT NULL,
--	name character varying(255) NOT NULL,
--	data character varying(4000),
--	primary key (workitemid, name)
--);

CREATE TABLE swworkhistory (
	id character varying(50) NOT NULL,
	userid character varying(30),
	type character varying(20),
	formid character varying(50),
	title character varying(4000),
	executedtime timestamp,
	primary key (id)
);

CREATE TABLE swworktypehistory (
	id character varying(50) NOT NULL,
	userid character varying(30),
	formid character varying(50),
	executedtime timestamp,
	primary key (id)
);

-- 도메인 데이터
CREATE TABLE swdomain (
	id character varying(50) NOT NULL,
	companyid character varying(100),
	formid character varying(50),
	formversion int,
	formname character varying(100),
	tblowner character varying(100),
	tblname character varying(100),
	keycolumn character varying(100),
	titlefieldid character varying(50),
	keyDuplicable bool default '0',
	masterid character varying(50),
	masterfieldid character varying(50),
	systemdomainyn char(1),
	publishmode character varying(10),
	primary key (id)
);



CREATE TABLE swremoveddomain (
	formid character varying(50) NOT NULL,
	tblname character varying(100),
	primary key(formid)
);

CREATE TABLE swdomainfield (
	id character varying(50) NOT NULL,
	domainid character varying(50),
	formfieldid character varying(50),
	formfieldpath character varying(255),
	formfieldname character varying(255),
	formfieldtype character varying(50),
	tablecolname character varying(50),
	reftblname character varying(50),
	arrayyn char(1),
	systemfieldyn char(1),
	uniqueyn character varying(1),
	disporder int,
	tablewidth float,
	primary key (id)
);

CREATE TABLE swdataref (
	id character varying(50),
	myformid character varying(50),
	myformfieldid character varying(20),
	myrecordid character varying(50),
	refformid character varying(50),
	refformfieldid character varying(20),
	refrecordid character varying(4000),
	primary key (id)
);

-- 가젯 정보
CREATE TABLE swgadget (
	id 	character varying(50) NOT NULL,
	companyid character varying(50),
	name character varying(255),
	userid character varying(50) NOT NULL,
	createdtime timestamp,
	modifier character varying(50),
	modifiedtime timestamp,
	type character varying(50),
	formid character varying(50),
	query character varying(4000),
	classname character varying(255),
	location character varying(255),
	enabledyn char(1),
	content text,
	primary key (id)
);

-- 권한 정보
CREATE TABLE swauthresource (
	id 	character varying(50) NOT NULL,
	companyid character varying(50),
	resourceid character varying(50) NOT NULL,	
	type int NOT NULL,
	authmode char(1) NOT NULL,
	permission character varying(20),	
	creator character varying(50),
	createdtime timestamp,
	modifier character varying(50),
	modifiedtime timestamp,
	primary key (id)
);

CREATE TABLE swauthuser (
	id character varying(50) NOT NULL,
	companyid character varying(50),		
	resourceid character varying(50) NOT NULL,
	authmode char(1) NOT NULL,
	userid character varying(50) NOT NULL,
	type character varying(20) NOT NULL,
	primary key (id)
);

-- 로고
CREATE TABLE swconfig (
	id character varying(50) NOT NULL,
	logo character varying(50),
	loginImage character varying(50),
	primary key (id)
);

---- 국제화
--CREATE TABLE swlanguage (
--	id character varying(255) NOT NULL,
--	kor character varying(255),
--	eng character varying(255),
--	primary key (id)
--);

CREATE TABLE aprapr (
	aprobjid character varying(100) NOT NULL,
    aprname character varying(100),
    aprcreateuser character varying(50),
    aprcreatedate timestamp without time zone,
    aprmodifyuser character varying(50),
    aprmodifydate timestamp without time zone,
    aprdesc text,
    aprstatus character varying(50),
    aprtype character varying(50),
    aprapprover character varying(50),
    aprDueDate character varying(100),
    aprismanda boolean,
    aprismodify boolean,
    approvalline character varying(100),
    aprseq int,
    primary key (aprobjid)
);

CREATE TABLE apraprextprop (
 	aprobjid character varying(100) NOT NULL,
    aprname character varying(100),
    aprvalue character varying(100),
    aprseq int NOT NULL,
    primary key (aprobjid, aprseq)
);

CREATE TABLE apraprline (
    aprobjid character varying(100) NOT NULL,
    aprname character varying(100),
    aprcreateuser character varying(50),
    aprcreatedate timestamp without time zone,
    aprmodifyuser character varying(50),
    aprmodifydate timestamp without time zone,
    aprdesc text,
    aprstatus character varying(50),
    aprcorr character varying(50),
    aprRefAppLineDefId character varying(50),
	primary key (aprobjid)
);

CREATE TABLE apraprlinedef(
	objid character varying(50) NOT NULL,
	aprlinename character varying(255) NULL,
	aprdescription character varying(50) NULL,
	aprlevel character varying(50) NULL,
	companyid character varying(255) NULL,
	creator character varying(50) NULL,
	createdtime timestamp NULL,
	modifier character varying(50) NULL,
	modifiedtime timestamp NULL,
	primary key (objid)
);

CREATE TABLE apraprlineextprop (
    aprobjid character varying(100) NOT NULL,
    aprname character varying(100),
    aprvalue character varying(100),
    aprseq int NOT NULL,
  	primary key (aprobjid, aprseq)
);

--CREATE TABLE chtchart (
--    chtobjid character varying(100) NOT NULL,
--    chtcharttype character varying(20) NOT NULL,
--    chtname character varying(50),
--    chtcreateuser character varying(255),
--    chtcreatedate timestamp without time zone,
--    chtmodifyuser character varying(255),
--    chtmodifydate timestamp without time zone,
--    chtobjstr text
--);

--CREATE TABLE chtchartextprop (
--    chtobjid character varying(100) NOT NULL,
--    chtname character varying(100),
--    chtvalue character varying(100),
--    chtseq int NOT NULL
--);

CREATE TABLE collist (
    colobjid character varying(100) NOT NULL,
    colname character varying(100),
    colcreateuser character varying(50),
    colcreatedate timestamp without time zone,
    colmodifyuser character varying(50),
    colmodifydate timestamp without time zone,
    coltype character varying(100),
    colcorr character varying(200),
    colstatus character varying(50),
    coldesc text,
    primary key(colobjid)
);

CREATE TABLE collistextprop (
    colobjid character varying(100) NOT NULL,
    tskname character varying(100),
    tskvalue character varying(100),
    colseq int NOT NULL,
    primary key(colobjid, colseq)
);

CREATE TABLE collistitem (
    colobjid character varying(100) NOT NULL,
    coltype character varying(100),
    colref character varying(100),
    collabel character varying(100),
    colexpr text,
    colseq int NOT NULL,
    primary key(colobjid, colseq)
);

CREATE TABLE lnklnk (
    lnkobjid character varying(100) NOT NULL,
    lnkname character varying(100),
    lnkcreateuser character varying(50),
    lnkcreatedate timestamp without time zone,
    lnkmodifyuser character varying(50),
    lnkmodifydate timestamp without time zone,
    lnktype character varying(100),
    lnkcorr character varying(400),
    lnkfromtype character varying(100),
    lnkfromref character varying(200),
    lnkfromlabel character varying(100),
    lnkfromexpr text,
    lnktotype character varying(100),
    lnktoref character varying(200),
    lnktolabel character varying(100),
    lnktoexpr text,
    lnkcondtype character varying(100),
    lnkcondexpr text,
    primary key(lnkobjid)
);

CREATE TABLE lnklnkextprop (
    lnkobjid character varying(100) NOT NULL,
    tskname character varying(100),
    tskvalue character varying(100),
    lnkseq int NOT NULL,
    primary key(lnkobjid, lnkseq)
);

CREATE TABLE colmap (
    colobjid character varying(100) NOT NULL,
    colname character varying(100),
    colcreateuser character varying(50),
    colcreatedate timestamp without time zone,
    colmodifyuser character varying(50),
    colmodifydate timestamp without time zone,
    coltype character varying(200),
    colfromtype character varying(200),
    colfromref character varying(200),
    coltotype character varying(200),
    coltoref character varying(200),
    primary key(colobjid)
);

CREATE TABLE colmapextprop (
    colobjid character varying(100) NOT NULL,
    tskname character varying(100),
    tskvalue character varying(100),
    colseq int NOT NULL,
    primary key(colobjid, colseq)
);

CREATE TABLE colvalue (
    colobjid character varying(100) NOT NULL,
    colname character varying(100),
    colcreateuser character varying(50),
    colcreatedate timestamp without time zone,
    colmodifyuser character varying(50),
    colmodifydate timestamp without time zone,
    coltype character varying(100),
    colref character varying(200),
    colexpdate timestamp without time zone,
    colstatus character varying(50),
    coldesc text,
    colvalue text,
    primary key(colobjid)
);

CREATE TABLE colvalueextprop (
    colobjid character varying(100) NOT NULL,
    tskname character varying(100),
    tskvalue character varying(100),
    colseq int NOT NULL,
    primary key(colobjid, colseq)
);

--CREATE TABLE mdlmodel (
--    mdlobjid character varying(100) NOT NULL,
--    mdlname character varying(50),
--    mdlcreateuser character varying(255),
--    mdlcreatedate timestamp without time zone,
--    mdlmodifyuser character varying(255),
--    mdlmodifydate timestamp without time zone,
--    mdlpkg character varying(50),
--    mdlprefix character varying(50),
--    mdlobjstr text,
--	  primary key(mdlobjid)
--);

--CREATE TABLE mdlmodelextprop (
--    mdlobjid character varying(100) NOT NULL,
--    mdlname character varying(100),
--    mdlvalue character varying(100),
--    mdlseq int NOT NULL,
--	  primary key(mdlobjid, mdlseq)
--);

CREATE TABLE prcprc (
    prcobjid character varying(100) NOT NULL,
    prcname character varying(100),
    prccreateuser character varying(50),
    prccreatedate timestamp without time zone,
    prcmodifyuser character varying(50),
    prcmodifydate timestamp without time zone,
    tskdesc text,
    prcdid character varying(100),
    prcdver character varying(100),
    prcprcid character varying(100),
    prcdiagram text,
    prcType character varying(100),
	primary key(prcobjid)
);

CREATE TABLE prcprcextprop (
    prcobjid character varying(100) NOT NULL,
    prcname character varying(100),
    prcvalue character varying(100),
    prcseq int NOT NULL,
	primary key(prcobjid, prcseq)
);

CREATE TABLE prcprcinst (
    prcobjid character varying(100) NOT NULL,
    prcname character varying(100),
    prccreateuser character varying(50),
    prccreatedate timestamp without time zone,
    prcmodifyuser character varying(50),
    prcmodifydate timestamp without time zone,
    prcstatus character varying(100),
    prctitle character varying(255),
    tskdesc text,
    tskpriority character varying(50),
    prcdid character varying(100),
    prcdver character varying(100),
    prcprcid character varying(100),
    prcdiagram text,
    issubinstance character varying(50) NULL,
    prcinstvariable text NULL,
    prcPackageId character varying(100),
    prcType character varying(100),
    prcWorkspaceId character varying(100),
    prcWorkSpaceType character varying(50),
    prcAccessLevel character varying(50),
    prcAccessValue character varying(4000),
	primary key(prcobjid)
);

CREATE TABLE prcprcinstextprop (
    prcobjid character varying(100) NOT NULL,
    prcname character varying(100),
    prcvalue character varying(100),
    prcseq int NOT NULL,
	primary key(prcobjid, prcseq)
);

CREATE TABLE tsktask (
    tskobjid character varying(100) NOT NULL,
    tskname character varying(100),
    tskcreateuser character varying(50),
    tskcreatedate timestamp without time zone,
    tskmodifyuser character varying(50),
    tskmodifydate timestamp without time zone,
    tskstatus character varying(50),
    tskcorr character varying(50),
    tsktype character varying(100),
    tskprcinstid character varying(50),
    tsktitle character varying(255),
    tskdesc text,
    tskpriority character varying(50),
    tskdoc text,
    tskassigner character varying(50),
    tskassignee character varying(50),
    tskperformer character varying(50),
    tskstartdate timestamp without time zone,
    tskassigndate timestamp without time zone,
    tskexecutedate timestamp without time zone,
    tskduedate timestamp without time zone,
    tskdef character varying(100),
    tskform character varying(100),
    tskmultiinstid character varying(100),
    tskmultiinstorder character varying(10),
    tskmultiinstflowcond character varying(10),
    tskstep int,
    tskloopcnt int,
    tskexpectstartdate timestamp without time zone NULL ,
    tskexpectenddate timestamp without time zone NULL ,
    tskrealstartdate timestamp without time zone NULL ,
    tskrealenddate timestamp without time zone NULL ,
    tskinstvariable text NULL,
    isStartActivity character varying(10),
    tskFromRefType character varying(50),
    tskFromRefId character varying(100),
    tskApprovalId character varying(100),
    tskForwardId character varying(100),
    tskIsApprovalSourceTask character varying(10),
    tskTargetApprovalStatus character varying(10),
    tskWorkspaceId character varying(100),
    tskWorkspaceType character varying(50),
    tskAccessLevel character varying(50),
    tskAccessValue character varying(4000),
    tskRefType character varying(100),
	primary key(tskobjid)
);


CREATE TABLE tsktaskdef (
    tskobjid character varying(100) NOT NULL,
    tskname character varying(100),
    tskcreateuser character varying(50),
    tskcreatedate timestamp without time zone,
    tskmodifyuser character varying(50),
    tskmodifydate timestamp without time zone,
    tskstatus character varying(50),
    tskcorr character varying(100),
    tsktype character varying(100),
    tskprcinstid character varying(100),
    tsktitle character varying(200),
    tskdesc text,
    tskpriority character varying(100),
    tskdoc text,
    tskassigner character varying(100),
    tskassignee character varying(100),
    tskassigndate character varying(100),
    tskduedate character varying(100),
    tskform character varying(100),
    tskmultiinstorder character varying(10),
    tskmultiinstflowcond character varying(10),
    tsksubflowtargetid	character varying(200) NULL,
	tsksubflowtargetver	character varying(10) NULL,
	tsksubflowexecution	character varying(50) NULL,
    tskservicetargetid character varying(100) NULL,
	primary key(tskobjid)
);

CREATE TABLE tsktaskdefextprop (
    tskobjid character varying(100) NOT NULL,
    tskname character varying(100),
    tskvalue character varying(100),
    tskseq int NOT NULL,
	primary key(tskobjid, tskseq)
);

CREATE TABLE tsktaskextprop (
    tskobjid character varying(100) NOT NULL,
    tskname character varying(100),
    tskvalue character varying(800),
    tskseq int NOT NULL,
	primary key(tskobjid, tskseq)
);

-- 공지사항
CREATE TABLE SWNotice (
	id character varying(50) NOT NULL,
	domainId character varying(50) NULL,
	workItemId character varying(50) NULL,
	masterRecordId character varying(50) NULL,
	creator character varying(50) NULL,
	createdTime timestamp without time zone NULL,
	modifier character varying(50) NULL,
	modifiedTime timestamp without time zone NULL,
	filegroupid character varying(255) NULL,
	title character varying(255) NULL,
	content text NULL,
	primary key (id)
);

-- 게시판
CREATE TABLE SwBoard (
	id 	character varying(50) NOT NULL,
	title character varying(255),
	priority character varying(10),
	content text,
	filegroupid character varying(255),
	domainid character varying(50),
	workitemid character varying(50),
	creator	character varying(50),
	createdtime timestamp without time zone,
	modifier	character varying(50),
	modifiedtime timestamp without time zone,
	notice bool,
	primary key (id)
);

-- 자료실
CREATE TABLE SWAttachment (
	id character varying(50) NOT NULL,
	domainid character varying(50) NULL,
	workitemid character varying(50) NULL,
	masterrecordid character varying(50) NULL,
	creator character varying(50) NULL,
	createdtime timestamp without time zone NULL,
	modifier character varying(50) NULL,
	modifiedtime timestamp without time zone NULL,
	content text NULL,
	filegroupid character varying(255) NULL,
	searchword character varying(255) NULL,
	ownerdept character varying(255) NULL,
	title character varying(255) NULL,
	owneruser character varying(255) NULL
);

-- 연락처
CREATE TABLE sworgcontact (
	id character varying(50) NOT NULL,
	name character varying(50),
	companyid character varying(50),
	deptid character varying(50),
	pos character varying(50),
	email character varying(100),
	telephone character varying(50),
	swcompphone character varying(50),
	swfax character varying(50),
	swaddress character varying(255),
	swcountry character varying(50),
	swcat character varying(50),
	swdesc text,
	swattach character varying(255),
	domainid character varying(50),
	workitemid character varying(50),
	creator	character varying(50),
	createdtime timestamp without time zone,
	modifier	character varying(50),
	modifiedtime timestamp without time zone,
	primary key (id)
);

-- 이벤트
CREATE TABLE SWEvent (
	id character varying(50) NOT NULL,
	domainId character varying(50) NULL,
	workItemId character varying(50) NULL,
	masterRecordId character varying(50) NULL,
	creator character varying(50) NULL,
	createdTime timestamp without time zone NULL,
	modifier character varying(50) NULL,
	modifiedTime timestamp without time zone NULL,
	enddate timestamp without time zone NULL,
	relatedusers character varying(4000) NULL, -- userField는 character varying(4000)으로 생성
	content text NULL,
	startdate timestamp without time zone NULL,
	name character varying(255) NULL,
	place character varying(255) NULL,
	alarm character varying(255) NULL,
	primary key (id)
);

-- 메모
CREATE TABLE SWMemo (
	id character varying(50) NOT NULL primary key,
	domainid character varying(50),
	workitemid character varying(50),
	masterrecordid character varying(50),
	creator character varying(50),
	createdtime timestamp without time zone,
	modifier character varying(50),
	modifiedtime timestamp without time zone,
	content text,
	title character varying(255)
);

----업무전달
--CREATE TABLE dt_1287553898892 (
--	id character varying(50) NOT NULL,
--	domainid character varying(50) NULL,
--	workitemid character varying(50) NULL,
--	masterrecordid character varying(50) NULL,
--	creator character varying(50) NULL,
--	createdtime timestamp without time zone NULL,
--	modifier character varying(50) NULL,
--	modifiedtime timestamp without time zone NULL,
--	c8 text NULL,
--	c12 character varying(255) NULL,
--	c4 character varying(255) NULL
--);

----업무보고
--CREATE TABLE dt_1287553898894(
--	id character varying(50)NOT NULL,
--	domainid character varying(50),
--	workitemid character varying(50),
--	masterrecordid character varying(50),
--	creator character varying(50),
--	createdtime timestamp without time zone ,
--	modifier character varying(50),
--	modifiedtime timestamp without time zone ,
--	c15 text,
--	c2 character varying(255),
--	c6 character varying(255),
--	c4 character varying(255),
--	c19 character varying(255),
--	c61 timestamp without time zone ,
--	c17 text,
--	c13 text,
--	c0 character varying(255),
--	c63 timestamp without time zone ,
--	c8 timestamp without time zone 
--);

--sw2.0 추가
CREATE TABLE swmenuitem(
	objid character varying(50) NOT NULL,
	name character varying(255) NULL,
	menuseqno int NULL,
	imgpath character varying(150) NULL,
	categoryid character varying(50) NULL,
	packageid character varying(50) NULL,
	packagetype character varying(50) NULL,
	groupid character varying(50) NULL,
	formid character varying(50) NULL,
	creator character varying(50) NULL,
	createdtime timestamp without time zone NULL,
	modifier character varying(50) NULL,
	modifiedtime timestamp without time zone NULL,
	itmseq int NOT NULL,
	primary key (objid, itmseq)
)
;

CREATE TABLE swmenuitemlist(
	objid character varying(50) NOT NULL ,
	companyid character varying(50) NULL,
	userid character varying(50) NULL,
	creator character varying(50) NULL,
	createdtime timestamp without time zone NULL,
	modifier character varying(50) NULL,
	modifiedtime timestamp without time zone NULL,
	primary key (objid)
)
;

CREATE TABLE sweventday(
	objid character varying(50) NOT NULL,
	name character varying(50),
	companyid character varying(100),
	type character varying(50),
	startday timestamp without time zone,
	endday timestamp without time zone,
	description character varying(4000),
	creator character varying(50),
	createdtime timestamp without time zone,
	modifier character varying(50),
	modifiedtime timestamp without time zone NULL,
	reltdperson character varying(4000),
	primary key (objid)
)
;

CREATE TABLE sworgteam(
	id character varying(50) NOT NULL,
	companyid character varying(50) NULL,
	name character varying(255) NULL,
	teamleader character varying(50) NULL,
	dept character varying(50) NULL,
	member text NULL,
	accesslevel character varying(50) NULL,
	state character varying(50) NULL,
	description character varying(4000) NULL,
	creator character varying(50) NULL,
	createdtime timestamp without time zone NULL,
	modifier character varying(50) NULL,
	modifiedtime timestamp without time zone NULL,
	primary key (id)
)
;

CREATE TABLE msgmessage(
	id character varying(50) NOT NULL,
	content character varying(2000) NULL,
	targetuser character varying(300) NULL,
	userid character varying(50) NULL,
	modifier character varying(50) NULL,
	modifiedtime timestamp without time zone NULL,
	createdtime timestamp without time zone NULL,
	primary key (id)
)
;

CREATE TABLE swworkhour (
	objid character varying(50) NOT NULL,
	companyid character varying(50),
	type character varying(50),
	startdayofweek character varying(50),
	workingdays int,
	validfromdate timestamp without time zone,
	validtodate timestamp without time zone,
	monstarttime timestamp without time zone,
	monendtime timestamp without time zone,
	tuestarttime timestamp without time zone,
	tueendtime timestamp without time zone,
	wedstarttime timestamp without time zone,
	wedendtime timestamp without time zone,
	thustarttime timestamp without time zone,
	thuendtime timestamp without time zone,
	fristarttime timestamp without time zone,
	friendtime timestamp without time zone,
	satstarttime timestamp without time zone,
	satendtime timestamp without time zone,
	sunstarttime timestamp without time zone,
	sunendtime timestamp without time zone,
	creator character varying(50),
	createdtime timestamp without time zone,
	modifier character varying(50),
	modifiedtime timestamp without time zone,
	primary key (objid)
)
;

CREATE TABLE sworgconfig(
	id character varying(50) NOT NULL,
	smtpaddress character varying(100) NULL,
	userid character varying(50) NULL,
	password character varying(20) NULL,
	name character varying(255) NULL,
	domainid character varying(50) NULL,
	companyid character varying(100) NULL,
	creator character varying(50) NULL,
	createdtime timestamp without time zone NULL,
	modifier character varying(50) NULL,
	modifiedtime timestamp without time zone NULL,
	isactivity int NULL,
	primary key (id)
)
;

--추가
CREATE TABLE apraprdef(  
	objid character varying(50),
	type character varying(255),
	aprname character varying(150),
	aprperson character varying(50),
	dueDate character varying(100),
	level character varying(50),
	creator character varying(50),
	createdtime timestamp without time zone NULL,
	modifier character varying(50),
	modifiedtime timestamp without time zone NULL,
	defseq int NOT NULL,
	constraint apraprdef_pkey primary key (objid, defseq),
	constraint fkaprapprovaldef foreign key (objid) references apraprlinedef (objid)
);


--CREATE TABLE docfile(  
--	id character varying(50),
--	companyid character varying(30),
--	type character varying(255),
--	filename character varying(100),
--	filepath character varying(200),
--	filesize decimal(19, 0) NULL,
--	writtentime timestamp without time zone NULL,
--	constrainteger docfile_pkey primary key (id)
--);


--CREATE TABLE lcslcs(  
--	lcsobjid character varying(100),
--	lcsname character varying(100),
--	lcscreateuser character varying(50),
--	lcscreatedate timestamp without time zone NULL,
--	lcsmodifyuser character varying(50),
--	lcsmodifydate timestamp without time zone NULL,
--	lcsusername character varying(50),
--	lcsprod character varying(50),
--	lcscurrentver character varying(50),
--	lcsprodkey character varying(100),
--	lcsdomain character varying(100),
--	lcsmacaddr character varying(100),
--	lcsusersize decimal(19, 0) NULL,
--	lcsexpiredate timestamp without time zone NULL,
--	lcsdesc text,
--	lcsstatus character varying(50),
--	constrainteger lcslcs_pkey primary key (lcsobjid)
--);


--CREATE TABLE lcslcsextprop(
--	lcsobjid character varying(100),
--	lcsname character varying(100),
--	lcsvalue character varying(100),
--	lcsseq integer NOT NULL,
--	constrainteger lcslcsextprop_pkey primary key (lcsobjid, lcsseq),
--	constrainteger fklcslcsextprop foreign key (lcsobjid)
--	references lcslcs (lcsobjid)
--);


CREATE TABLE prcprcinstrel(
	prcobjid character varying(100),
	prccreateuser character varying(50),
	prccreatedate timestamp without time zone NULL,
	prcmodifyuser character varying(50),
	prcmodifydate timestamp without time zone NULL,
	prctype character varying(50),
	prcinstanceid character varying(100),
	prcparentinstid character varying(100),
	primary key (prcobjid)
);


--CREATE TABLE prcprcinstrelextprop(  
--	prcobjid character varying(100),
--	prcname character varying(100),
--	prcvalue character varying(100),
--	prcseq integer NOT NULL,
--	constrainteger prcprcinstrelextprop_pkey primary key (prcobjid, prcseq),
--	constrainteger fkprcprcinstrelextprop foreign key (prcobjid)
--	references prcprcinstrel (prcobjid)
--);


--CREATE TABLE prcprcinstvariable(  
--	prcobjid character varying(100),
--	prcname character varying(100),
--	prccreateuser character varying(50),
--	prccreatedate timestamp without time zone NULL,
--	prcmodifyuser character varying(50),
--	prcmodifydate timestamp without time zone NULL,
--	prctype character varying(50),
--	prcprcinstid character varying(100),
--	prcvariableid character varying(100),
--	prcrequired tinyinteger NULL,
--	prcmode character varying(50),
--	prcvalue text,
--        constrainteger prcprcinstvariable_pkey primary key (prcobjid)
--);


--CREATE TABLE prcprcinstvariableextprop(  
--	prcobjid character varying(100),
--	prcname character varying(100),
--	prcvalue character varying(100),
--	prcseq integer NOT NULL,
--	constrainteger prcprcinstvariableextprop_pkey primary key (prcobjid, prcseq),
--	constrainteger fkprcprcinstvariableextprop foreign key (prcobjid)
--	references prcprcinstvariable (prcobjid)
--);


--CREATE TABLE prcprcvariable(
--	prcobjid character varying(100),
--	prcname character varying(100),
--	prccreateuser character varying(50),
--	prccreatedate timestamp without time zone NULL,
--	prcmodifyuser character varying(50),
--	prcmodifydate timestamp without time zone NULL,
--	prctype character varying(50),
--	prcprcid character varying(100),
--	prcinitialvalue character varying(100),
--	prcrequired tinyinteger NULL,
--	prcmode character varying(50),
--	constrainteger prcprcvariable_pkey primary key (prcobjid)
--);


--CREATE TABLE prcprcvariableextprop(  
--	prcobjid character varying(100),
--	prcname character varying(100),
--	prcvalue character varying(100),
--	prcseq integer NOT NULL,
--	constrainteger prcprcvariableextprop_pkey primary key (prcobjid, prcseq),
--	constrainteger fkprcprcvariableextprop foreign key (prcobjid)
--	references prcprcvariable (prcobjid)
--);


--CREATE TABLE swcustomer(
--	id character varying(50),
--	status character varying(30),
--	name character varying(255),
--	customercode character varying(100),
--	smartworksurl character varying(200),
--	description text,
--	creator character varying(50),
--	createdtime timestamp without time zone NULL,
--	modifier character varying(50),
--	modifiedtime timestamp without time zone NULL,
--	constrainteger swcustomer_pkey primary key (id)
--);

  
--CREATE TABLE swcustomerextprop(
--	id character varying(50),
--	swcname character varying(100),
--	swcvalue character varying(100),
--	swcseq integer NOT NULL,
--	constrainteger swcustomerextprop_pkey primary key (id, swcseq),
--	constrainteger fkswccustomerextprop foreign key (id)
--	references swcustomer (id)
--);

  
CREATE TABLE sworguser_backup(  
	id character varying(50),
	companyid character varying(50),
	deptid character varying(50),
	roleid character varying(50),
	authid character varying(50),
	empno character varying(50),
	name character varying(255),
	type character varying(50),
	pos character varying(50),
	email character varying(50),
	passwd character varying(20),
	lang character varying(20),
	stdtime character varying(20),
	picture character varying(50),
	domainid character varying(50),
	workitemid character varying(50),
	creator character varying(50),
	createdtime timestamp without time zone NULL,
	modifier character varying(50),
	modifiedtime timestamp without time zone NULL,
	retiree character varying(50),
	primary key (id)
);


CREATE TABLE sworguserdetail(  
	id character varying(50),
	companyid character varying(50),
	empno character varying(50),
	name character varying(255),
	telcompany character varying(50),
	mobile character varying(50),
	telhome character varying(50),
	primary key (id)
);


--CREATE TABLE swproduct(  
--	id character varying(50),
--	name character varying(255),
--	creator character varying(50),
--	createdtime timestamp without time zone NULL,
--	modifier character varying(50),
--	modifiedtime timestamp without time zone NULL,
--	description text,
--	status character varying(30),
--	type character varying(20),
--	company character varying(100),
--	price character varying(50),
--	score float NULL,
--	mainimg character varying(255),
--	value text,
--	businesstypectgid character varying(100),
--	businessctgid character varying(100),
--	hitcount decimal(19, 0) NULL,
--	downcount decimal(19, 0) NULL,
--	ispublished character varying(20),
--	productcode character varying(50),
--	packagerels text,
--	extvalue text,
--	constrainteger swproduct_pkey primary key (id)
--);


--CREATE TABLE swproductextprop(  
--	id character varying(50),
--	swmname character varying(100),
--	swmvalue character varying(100),
--	swmseq integer NOT NULL,
--	constrainteger swproductextprop_pkey primary key (id, swmseq),
--	constrainteger fkswmproductextprop foreign key (id)
--	references swproduct (id)
--);


--CREATE TABLE swproductfile(  
--	id character varying(50),
--	type character varying(20),
--	productid character varying(100),
--	filetype character varying(20),
--	filename character varying(100),
--	filepath character varying(200),
--	filesize character varying(50),
--	creator character varying(50),
--	createdtime timestamp without time zone NULL,
--	modifier character varying(50),
--	modifiedtime timestamp without time zone NULL,	
--	constrainteger swproductfile_pkey primary key (id)
--);


--CREATE TABLE swproductprop(  
--	id character varying(50),
--	swmtype character varying(20),
--	swmname character varying(100),
--	swmimageid character varying(100),
--	swmdesc text,
--	swmimage text,
--	swmimagetn text,
--	swmseq integer NOT NULL,
--	constrainteger swproductprop_pkey primary key (id, swmseq),
--	constrainteger fkswmproductproperty foreign key (id)
--	references swproduct (id)
--);


CREATE TABLE swwebappservice(  
	objid character varying(50),
	webappservicename character varying(100),
	webappserviceurl character varying(100),
	modifymethod character varying(100),
	viewmethod character varying(100),
	description character varying(100),
	companyid character varying(100),
	primary key (objid)
);


CREATE TABLE swwebappserviceparameter(  
	objid character varying(50),
	variablename character varying(150),
	parametername character varying(255),
	parametertype character varying(150),
	type character varying(50),
	webseq int NOT NULL,
	constraint swwebappserviceparameter_pkey primary key (objid, webseq),
	constraint fkwebappservice foreign key (objid)
	references swwebappservice (objid)
);


CREATE TABLE swwebservice(  
	objid character varying(50),
	webservicename character varying(100),
	webserviceaddress character varying(100),
	wsdladdress character varying(100),
	portname character varying(100),
	operationname character varying(100),
	description character varying(100),
	companyid character varying(100),
	primary key (objid)
);


CREATE TABLE swwebserviceparameter(  
	objid character varying(50),
	variablename character varying(150),
	parametername character varying(255),
	parametertype character varying(150),
	type character varying(50),
	webseq int NOT NULL,
	constraint swwebserviceparameter_pkey primary key (objid, webseq),
	constraint fkwebservice foreign key (objid)
	references swwebservice (objid)
);

--CREATE TABLE updupd(  
--	updobjid character varying(100),
--	updname character varying(100),
--	updcreateuser character varying(50),
--	updcreatedate timestamp without time zone NULL,
--	updmodifyuser character varying(50),
--	updmodifydate timestamp without time zone NULL,
--	upddesc text,
--	updstatus character varying(50),
--	updtype character varying(50),
--	updmethod character varying(50),
--	updsrc text,
--	updtgt text,	
--	constrainteger updupd_pkey primary key (updobjid)
--);
--
--
--CREATE TABLE updupdextprop(  
--	updobjid character varying(100),
--	updname character varying(100),
--	updvalue character varying(100),
--	updseq integer NOT NULL,
--	constrainteger updupdextprop_pkey primary key (updobjid, updseq),
--	constrainteger fkupdupdextprop foreign key (updobjid)
--	references updupd (updobjid)
--);

-- 사용자 업무접근 로그 (2011.10.13 Add)
--CREATE TABLE SWActionLog(
--	userId character varying(50) NOT NULL,
--	userName character varying(50) NULL,
--	deptName character varying(50) NULL,
--	position character varying(50) NULL,
--	categoryName character varying(100) NULL,
--	menuName character varying(100) NULL,
--	menuType character varying(100) NULL,
--	actionType character varying(50) NULL,
--	actionTime timestamp without time zone NULL
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

--ALTER TABLE [ChtChartExtProp] WITH NOCHECK ADD 
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

--ALTER TABLE [LnkListExtProp] WITH NOCHECK ADD 
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

--ALTER TABLE [PrcPrcInstExtProp] WITH NOCHECK ADD 
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
-- CREATE  INDEX [IxChtChartExtProp2] ON [ChtChartExtProp]([chtValue]) ON [PRIMARY]
--GO
--
-- CREATE  INDEX [IxChtChartExtProp] ON [ChtChartExtProp]([chtName]) ON [PRIMARY]
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
-- CREATE  INDEX [IxLnkListExtProp1] ON [LnkListExtProp]([tskValue]) ON [PRIMARY]
--GO
--
-- CREATE  INDEX [IxLnkListExtProp] ON [LnkListExtProp]([tskName]) ON [PRIMARY]
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
-- CREATE  INDEX [IxPrcPrcInstExtProp] ON [PrcPrcInstExtProp]([prcName]) ON [PRIMARY]
--GO
--
-- CREATE  INDEX [IxPrcPrcInstExtProp2] ON [PrcPrcInstExtProp]([prcValue]) ON [PRIMARY]
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
--	CONSTRAinteger [FkAprAprLineAprs] FOREIGN KEY 
--	(
--		[approvalLine]
--	) REFERENCES [dbo].[AprAprLine] (
--		[aprObjId]
--	)
--GO
--
--ALTER TABLE [dbo].[AprAprExtProp] ADD 
--	CONSTRAinteger [FkAprAprExtProp] FOREIGN KEY 
--	(
--		[aprObjId]
--	) REFERENCES [dbo].[AprApr] (
--		[aprObjId]
--	)
--GO
--
--ALTER TABLE [dbo].[AprAprLineExtProp] ADD 
--	CONSTRAinteger [FkAprAprLineExtProp] FOREIGN KEY 
--	(
--		[aprObjId]
--	) REFERENCES [dbo].[AprAprLine] (
--		[aprObjId]
--	)
--GO
--
--ALTER TABLE [ChtChartExtProp] ADD 
--	CONSTRAinteger [FkChtChartExtProp] FOREIGN KEY 
--	(
--		[chtObjId]
--	) REFERENCES [ChtChart] (
--		[chtObjId]
--	)
--GO
--
--ALTER TABLE [LnkListExtProp] ADD 
--	CONSTRAinteger [FkLnkListExtProp] FOREIGN KEY 
--	(
--		[lnkObjId]
--	) REFERENCES [LnkList] (
--		[lnkObjId]
--	)
--GO
--
--ALTER TABLE [LnkListItem] ADD 
--	CONSTRAinteger [FkLnkListItem] FOREIGN KEY 
--	(
--		[lnkObjId]
--	) REFERENCES [LnkList] (
--		[lnkObjId]
--	)
--GO
--
--ALTER TABLE [LnkLnkExtProp] ADD 
--	CONSTRAinteger [FkLnkLnkExtProp] FOREIGN KEY 
--	(
--		[lnkObjId]
--	) REFERENCES [LnkLnk] (
--		[lnkObjId]
--	)
--GO
--
--ALTER TABLE [LnkMapExtProp] ADD 
--	CONSTRAinteger [FkLnkMapExtProp] FOREIGN KEY 
--	(
--		[lnkObjId]
--	) REFERENCES [LnkMap] (
--		[lnkObjId]
--	)
--GO
--
--ALTER TABLE [LnkValueExtProp] ADD 
--	CONSTRAinteger [FkLnkValueExtProp] FOREIGN KEY 
--	(
--		[lnkObjId]
--	) REFERENCES [LnkValue] (
--		[lnkObjId]
--	)
--GO
--
--ALTER TABLE [MdlModelExtProp] ADD 
--	CONSTRAinteger [FkMdlModelExtProp] FOREIGN KEY 
--	(
--		[mdlObjId]
--	) REFERENCES [MdlModel] (
--		[mdlObjId]
--	)
--GO
--
--ALTER TABLE [PrcPrcExtProp] ADD 
--	CONSTRAinteger [FkPrcPrcExtProp] FOREIGN KEY 
--	(
--		[prcObjId]
--	) REFERENCES [PrcPrc] (
--		[prcObjId]
--	)
--GO
--
--ALTER TABLE [PrcPrcInstExtProp] ADD 
--	CONSTRAinteger [FkPrcPrcInstExtProp] FOREIGN KEY 
--	(
--		[prcObjId]
--	) REFERENCES [PrcPrcInst] (
--		[prcObjId]
--	)
--GO
--
--ALTER TABLE [TskTaskDefExtProp] ADD 
--	CONSTRAinteger [FkTskTaskDefExtProp] FOREIGN KEY 
--	(
--		[tskObjId]
--	) REFERENCES [TskTaskDef] (
--		[tskObjId]
--	)
--GO
--
--ALTER TABLE [TskTaskExtProp] ADD 
--	CONSTRAinteger [FkTskTaskExtProp] FOREIGN KEY 
--	(
--		[tskObjId]
--	) REFERENCES [TskTask] (
--		[tskObjId]
--	)
--GO

-- 3.0 추가


-- 커뮤니티 그룹
CREATE TABLE SWOrgGroup (
	id	character varying(50) not null,
	companyId	character varying(50),
	name	character varying(100),
	groupLeader	character varying(50),
	groupType	character varying(1),
	status	character varying(1),
	picture character varying(100),
	description	character varying(4000),
	maxMember int,
	autoApproval bool,
	creator	character varying(50),
	createdTime	timestamp without time zone,
	modifier	character varying(50),
	modifiedTime	timestamp without time zone,
	primary key (id)
);

-- 커뮤니티 그룹 멤버
CREATE TABLE SWOrgGroupMember (
	groupId	character varying(50) not null,
	userId	character varying(50) not null,
	joinType	character varying(1),
	joinStatus	character varying(1),
	joinTime	timestamp without time zone,
	outTime 	timestamp without time zone,
	memberSeq	int,
	creator character varying(50),
	createdtime timestamp without time zone ,
	modifier character varying(50),
	modifiedtime timestamp without time zone ,
	primary key (groupId, userId)
);

-- 폴더
CREATE TABLE SwFolder (
	id character varying(50) NOT NULL,
	companyid character varying(100),
	parentid character varying(50),
	name character varying(255),
	disporder int,
	description character varying(4000),
	creator character varying(30),
	createdtime timestamp without time zone,
	modifier character varying(30),
	modifiedtime timestamp without time zone,
	tskWorkspaceId character varying(50),
	tskRefType character varying(50),
	primary key (id)
);

-- 폴더파일
CREATE TABLE SwFolderFile (
	folderId character varying(50) NOT NULL,
	fileId character varying(50) NOT NULL,
	fileSeq int,
  	primary key(folderId, fileId)
);

-- 좋아요
CREATE TABLE SwLike (
	id character varying(50) NOT NULL,
	refType int,
	refId character varying(50),
	creator character varying(50),
	createdTime timestamp without time zone,
	primary key (id)
);

-- 메세지
CREATE TABLE SWMessage(
	id character varying(50) NOT NULL,
	content character varying(4000),
	sendUserId character varying(50),
	targetUserId character varying(50),
	deleteUserId character varying(50),
	isChecked bool,
	chatId character varying(50),
	chattersId character varying(2000),
	checkedTime timestamp without time zone,
	creator character varying (50),
	createdTime timestamp without time zone,
	modifier character varying (50),
	modifiedTime timestamp without time zone,
	primary key (id)
);


-- 로그인 사용자
CREATE TABLE SwLoginUser (
	userId character varying(50) NOT NULL,
	loginTime timestamp without time zone,
	primary key (userId)
);
CREATE SEQUENCE folder_db_objects_seq INCREMENT BY 1 NO MAXVALUE NO MINVALUE CACHE 1;
--메일관련 테이블 Start
CREATE TABLE folder_db_objects (
    id bigint DEFAULT nextval('folder_db_objects_seq'::regclass) NOT NULL,
    username character varying(255) NOT NULL,
    parent_id bigint NOT NULL,
    folder_name character varying(100) NOT NULL,
    folder_type int NOT NULL,
    primary key (id)
);
CREATE SEQUENCE msg_db_objects_seq INCREMENT BY 1 NO MAXVALUE NO MINVALUE CACHE 1;
CREATE TABLE msg_db_objects (
    id bigint DEFAULT nextval('msg_db_objects_seq'::regclass) NOT NULL,
    uid character varying(100),
    username character varying(255) NOT NULL,
    folder_id bigint NOT NULL,
    unique_id character varying(100) NOT NULL,
    sender character varying(255),
    receiver text,
    cc text,
    bcc text,
    replyTo character varying(255),
    subject character varying(255),
    multipart smallint,
    priority int,
    sentdate timestamp without time zone,
    unread smallint NOT NULL,
    msg_size bigint NOT NULL,
    email bytea NOT NULL,
    primary key (id)
);

CREATE TABLE msg_db_uids (
    username character varying(255) NOT NULL,
    uid character varying(100) NOT NULL
);
CREATE SEQUENCE msg_rules_seq START WITH 1 INCREMENT BY 1 NO MAXVALUE NO MINVALUE CACHE 1;

CREATE TABLE msg_rules (
    id bigint DEFAULT nextval('msg_rules_seq'::regclass) NOT NULL,
    username character varying(255) NOT NULL,
    portion character varying(100) NOT NULL,
    rule_condition character varying(30) NOT NULL,
    keyword character varying(255) NOT NULL,
    rule_action character varying(30) NOT NULL,
    destination character varying(100) NOT NULL,
    primary key (id)
);
--메일관련 테이블 End

--알림 카운트를 세고 알림 조회를 위한 테이블
CREATE TABLE SWPublishNotice (
	id character varying(50) NOT NULL,
	type int,
	refType character varying(50),
	refId character varying(100),
	assignee character varying(50),
	creator character varying (50),
	createdTime timestamp without time zone,
	modifier character varying (50),
	modifiedTime timestamp without time zone,
	primary key (id)
);


-- SwMailServer (메일서버정보)
CREATE TABLE SwMailServer (
	id character varying(50) NOT NULL,
	name character varying(100) NOT NULL,
	companyId character varying(50) NOT NULL,
	fetchServer character varying(50),
	fetchServerPort int,
	fetchProtocol character varying(10),
	fetchSsl bool,
	smtpServer character varying(50),
	smtpServerPort int,
	smtpAuthenticated bool,
	smtpSsl bool,
	creator	character varying(50),
	createdtime timestamp without time zone,
	modifier character varying(50),
	modifiedtime timestamp without time zone,
    primary key (id)
);

-- SwMailAccount (사용자메일계정)
CREATE TABLE SwMailAccount (
	id character varying(50) NOT NULL,
	userId character varying(50) NOT NULL,
	mailServerId character varying(50) NOT NULL,
	mailServerName character varying(100) NOT NULL,
	mailId character varying(50) NOT NULL,
	mailUserName character varying(50),
	mailPassword character varying(50) NOT NULL,
	mailSignature character varying(4000),
	useMailSign bool,
	senderUserTitle character varying(50),
	mailDeleteFetched character varying(10),
	creator	character varying(50),
	createdtime timestamp without time zone,
	modifier character varying(50),
	modifiedtime timestamp without time zone,
    primary key (id)
);

-- 아이디 자동생성
CREATE TABLE SwAutoIndexDef (
	objId character varying(50) NOT NULL,
	formId character varying(100) NOT NULL,
	version int,
	fieldId character varying(10) NOT NULL,	
	creator	character varying(50),
	createdtime timestamp,
	modifier character varying(50),
	modifiedtime timestamp,
    primary key (objId)
);

CREATE TABLE SwAutoIndexRuls (
	objId character varying(50) NOT NULL,
	ruleId character varying(100),
	type character varying(50),
	codeValue character varying(100),
	seperator character varying(10),
	increment int,
	incrementBy character varying(50),
	digits character varying(10),
	items character varying(500),
	indexSeq int NOT NULL,	
    primary key (objId, indexSeq)
);

CREATE TABLE SwAutoIndexSeq (
	objId character varying(50) NOT NULL,
	instanceId character varying(100),
	formId character varying(100),
	fieldId character varying(10),
	refType character varying(100),
	idType character varying(50),
	idValue character varying(200),
	seq int,
	creator	character varying(50),
	createdtime timestamp,
	modifier character varying(50),
	modifiedtime timestamp,
    primary key (objId)
);
--부서 권한
CREATE TABLE SwAuthDepartment (
	id character varying(50) NOT NULL,
	deptId character varying(100),
	deptAuthType character varying(50),
	roleKey character varying(50),
	customUser character varying(4000),
    primary key (id)
);
--그룹 권한
CREATE TABLE SwAuthGroup (
	id character varying(50) NOT NULL,
	groupId character varying(100),
	groupAuthType character varying(50),
	roleKey character varying(50),
	customUser character varying(4000),
    primary key (id)
);
CREATE TABLE SWFileDownHistory (
	id character varying(50) NOT NULL,
	fileId character varying(50),
	fileName character varying(255),
	downloadUserId character varying(50),
	refPackageId character varying(100),
	refPackageName character varying(255),
	refPrcInstId character varying(100),
	refPrcInstName character varying(200),
	refTaskId character varying(50),
	refTaskName character varying(200),
	creator	character varying(50),
	createdtime timestamp,
	modifier character varying(50),
	modifiedtime timestamp,
    primary key (id)
);

CREATE TABLE SwLoginUserHistory (
	id character varying(50) NOT NULL,
	userId character varying(50),
	loginTime timestamp,
    primary key (id)
);

CREATE TABLE SwAutoIndexInst (
	objId character varying(50) NOT NULL,
	instanceId character varying(100),
	formId character varying(100),
	fieldId character varying(10),
	refType character varying(10),
	idType character varying(50),
	idValue character varying(200),
	seq int,
	creator	character varying(50),
	createdtime timestamp,
	modifier character varying(50),
	modifiedtime timestamp,
    primary key (objId)
);



CREATE TABLE SwReport (
	id character varying(50) NOT NULL,
	name character varying(100),
	type int,
	targetWorkId character varying(50),
	targetWorkType int,
	dataSourceType int,
	externalServiceId character varying(50),
	reportTableKey character varying(50),
	searchFilterId character varying(50),
	owner character varying(50),
	chartType int,
	
	xAxis character varying(50),
	xAxisSelector character varying(50),
	xAxisSort character varying(50),
	xAxisMaxRecords int,
	xSecondAxis character varying(50),
	xSecondAxisSelector character varying(50),
	xSecondAxisSort character varying(50),
	yAxis character varying(50),
	yAxisSelector character varying(50),
	valueType character varying(50),
	zAxis character varying(50),
	zAxisSelector character varying(50),
	zAxisSort character varying(50),
	zSecondAxis character varying(50),
	zSecondAxisSelector character varying(50),
	zSecondAxisSort character varying(50),
	workspaceid character varying(100),
	workspacetype character varying(50),
	accesslevel character varying(50),
	accessvalue character varying(4000),
	creator	character varying(50),
	createdtime timestamp,
	modifier character varying(50),
	modifiedtime timestamp,
    primary key (id)
);


CREATE TABLE SwReportPane (
	id character varying(50) NOT NULL,
	name character varying(100),
	columnSpans int,
	position int,
	reportId character varying(50),
	reportName character varying(100),
	reportType int,
	targetWorkId character varying(50),
	chartType int,
	isChartView boolean,
	isStacked boolean,
	showLegend boolean,
	stringLabelRotation character varying(50),
	owner character varying(50),
	creator	character varying(50),
	createdtime timestamp,
	modifier character varying(50),
	modifiedtime timestamp,
    primary key (id)
);


