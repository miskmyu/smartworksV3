CREATE TABLE CourseDetail (
	courseId varchar(50) NOT NULL,
	object varchar(500),
	categories varchar(500),
	keywords varchar(500),
	duration int,
	startDate datetime,
	endDate datetime,
	maxMentees int,
	autoApproval tinyint,
	payable tinyint,
	fee int,
	teamId varchar(100),
	targetPoint int,
	achievedPoint int,
	recommended bit,
	lastMissionIndex int,
	createDate datetime,
	coursePoint int,
	primary key (courseId)
);
CREATE TABLE MentorDetail (
	mentorId varchar(50) NOT NULL,
	born varchar(500),
	homeTown varchar(500),
	living varchar(500),
	family varchar(100),
	works varchar(100),
	mentorHistory varchar(500),
	menteeHistory varchar(500),
	lectures varchar(500),
	awards varchar(500),
	etc varchar(500),
	educations varchar(500),
	primary key (mentorId)
);

--Mission Tabel
CREATE TABLE Mission (
	id varchar(50)NOT NULL,
	domainid varchar(50),
	workitemid varchar(50),
	masterrecordid varchar(50),
	creator varchar(50),
	createdtime datetime ,
	modifier varchar(50),
	modifiedtime datetime ,
	missionClearers varchar(255),
	closeDate datetime ,
	openDate datetime ,
	prevMission varchar(255),
	idx varchar(255),
	title varchar(255),
	starPoint varchar(255),
	content text,
	primary key (id)
);
INSERT INTO swpackage (id, categoryid, packageid, version, name, type, status, latestdeployedyn, creator, createdtime, modifier, modifiedtime, description) VALUES ('402880e3368c3af901368c3ccd690004', '40288afb1b25f00b011b25f3c7950001', 'pkg_dc3edb6efa47418cbd1f8fef889b4818', 1, '미션관리', 'SINGLE', 'DEPLOYED', 'Y', 'admin', getdate(), 'admin', getdate(), '미션관리');

INSERT INTO swdomain (id, companyId, formid, formversion, formname, tblowner, tblname, keycolumn, titlefieldid, masterid, masterfieldid, systemdomainyn, publishmode) VALUES ('sera_mission','Maninsoft', 'sera_mission', 1, '미션관리', '', 'Mission', 'id', '7', NULL, NULL, 'Y', 'PUB_ALL');

INSERT INTO swdomainfield (id, domainid, formfieldid, formfieldpath, formfieldname, formfieldtype, tablecolname, reftblname, arrayyn, systemfieldyn, uniqueyn, disporder, tablewidth) VALUES ('sera_content', 'sera_mission', '6', NULL, 'content', 'text', 'content', NULL, 'N', 'N', NULL, 5, 0);
INSERT INTO swdomainfield (id, domainid, formfieldid, formfieldpath, formfieldname, formfieldtype, tablecolname, reftblname, arrayyn, systemfieldyn, uniqueyn, disporder, tablewidth) VALUES ('sera_missionClearers', 'sera_mission', '4', NULL, 'missionClearers', 'string', 'missionClearers', NULL, 'N', 'N', NULL, -1, 0);
INSERT INTO swdomainfield (id, domainid, formfieldid, formfieldpath, formfieldname, formfieldtype, tablecolname, reftblname, arrayyn, systemfieldyn, uniqueyn, disporder, tablewidth) VALUES ('sera_closeDate', 'sera_mission', '3', NULL, 'closeDate', 'datetime', 'closeDate', NULL, 'N', 'N', NULL, 1, 0);
INSERT INTO swdomainfield (id, domainid, formfieldid, formfieldpath, formfieldname, formfieldtype, tablecolname, reftblname, arrayyn, systemfieldyn, uniqueyn, disporder, tablewidth) VALUES ('sera_prevMission', 'sera_mission', '1', NULL, 'prevMission', 'string', 'prevMission', NULL, 'N', 'N', NULL, 3, 0);
INSERT INTO swdomainfield (id, domainid, formfieldid, formfieldpath, formfieldname, formfieldtype, tablecolname, reftblname, arrayyn, systemfieldyn, uniqueyn, disporder, tablewidth) VALUES ('sera_index', 'sera_mission', '0', NULL, 'index', 'string', 'idx', NULL, 'N', 'N', NULL, 4, 0);
INSERT INTO swdomainfield (id, domainid, formfieldid, formfieldpath, formfieldname, formfieldtype, tablecolname, reftblname, arrayyn, systemfieldyn, uniqueyn, disporder, tablewidth) VALUES ('sera_title', 'sera_mission', '7', NULL, 'title', 'string', 'title', NULL, 'N', 'N', NULL, -1, 0);
INSERT INTO swdomainfield (id, domainid, formfieldid, formfieldpath, formfieldname, formfieldtype, tablecolname, reftblname, arrayyn, systemfieldyn, uniqueyn, disporder, tablewidth) VALUES ('sera_starPoint', 'sera_mission', '5', NULL, 'starPoint', 'string', 'starPoint', NULL, 'N', 'N', NULL, 6, 0);
INSERT INTO swdomainfield (id, domainid, formfieldid, formfieldpath, formfieldname, formfieldtype, tablecolname, reftblname, arrayyn, systemfieldyn, uniqueyn, disporder, tablewidth) VALUES ('sera_openDate', 'sera_mission', '2', NULL, 'openDate', 'datetime', 'openDate', NULL, 'N', 'N', NULL, 2, 0);

INSERT INTO swform (id, packageid, formid, version, name, type, status, creator, createdtime, modifier, modifiedtime, content)
VALUES ('402880e3368c3af901368c3ccd6e0005', 'pkg_dc3edb6efa47418cbd1f8fef889b4818', 'sera_mission', 1, '미션관리', 'SINGLE', 'DEPLOYED', 'admin', getdate(), 'admin', getdate(), '<form id="sera_mission" version="1" name="미션관리">
	<children>
		<formEntity id="0" name="index" systemType="string"
			required="false" system="false">
			<children />
			<format type="textInput" viewingType="textInput"></format>
			<graphic hidden="false" readOnly="false" labelWidth="123"
				contentWidth="293" height="23" cellSize="1" fitWidth="false"
				verticalScrollPolicy="true" textAlign="left" fitToScreen="false"
				listEditable="false" multipleUsers="false" />
		</formEntity>
		<formEntity id="1" name="prevMission" systemType="string"
			required="false" system="false">
			<children />
			<format type="textInput" viewingType="textInput"></format>
			<graphic hidden="false" readOnly="false" labelWidth="123"
				contentWidth="293" height="23" cellSize="1" fitWidth="false"
				verticalScrollPolicy="true" textAlign="left" fitToScreen="false"
				listEditable="false" multipleUsers="false" />
		</formEntity>
		<formEntity id="2" name="openDate" systemType="datetime"
			required="false" system="false">
			<children />
			<format type="dateChooser" viewingType="dateChooser">
				<date yearUse="false" sunNotUse="false" monNotUse="false"
					tueNotUse="false" wedNotUse="false" thuNotUse="false" friNotUse="false"
					satNotUse="false" />
			</format>
			<graphic hidden="false" readOnly="false" labelWidth="123"
				contentWidth="201" height="23" cellSize="1" fitWidth="false"
				verticalScrollPolicy="true" textAlign="left" fitToScreen="false"
				listEditable="false" multipleUsers="false" />
		</formEntity>
		<formEntity id="3" name="closeDate" systemType="datetime"
			required="false" system="false">
			<children />
			<format type="dateChooser" viewingType="dateChooser">
				<date yearUse="false" sunNotUse="false" monNotUse="false"
					tueNotUse="false" wedNotUse="false" thuNotUse="false" friNotUse="false"
					satNotUse="false" />
			</format>
			<graphic hidden="false" readOnly="false" labelWidth="123"
				contentWidth="201" height="23" cellSize="1" fitWidth="false"
				verticalScrollPolicy="true" textAlign="left" fitToScreen="false"
				listEditable="false" multipleUsers="false" />
		</formEntity>
		<formEntity id="4" name="missionClearers" systemType="string"
			required="false" system="false">
			<children />
			<format type="textInput" viewingType="textInput"></format>
			<graphic hidden="false" readOnly="false" labelWidth="123"
				contentWidth="293" height="23" cellSize="1" fitWidth="false"
				verticalScrollPolicy="true" textAlign="left" fitToScreen="false"
				listEditable="false" multipleUsers="false" />
		</formEntity>
		<formEntity id="5" name="starPoint" systemType="string"
			required="false" system="false">
			<children />
			<format type="textInput" viewingType="textInput"></format>
			<graphic hidden="false" readOnly="false" labelWidth="123"
				contentWidth="293" height="23" cellSize="1" fitWidth="false"
				verticalScrollPolicy="true" textAlign="left" fitToScreen="false"
				listEditable="false" multipleUsers="false" />
		</formEntity>
		<formEntity id="6" name="content" systemType="text"
			required="false" system="false">
			<children />
			<format type="richEditor" viewingType="richEditor"></format>
			<graphic hidden="false" readOnly="false" labelWidth="123"
				contentWidth="773" height="83" cellSize="1" fitWidth="false"
				verticalScrollPolicy="true" textAlign="left" fitToScreen="false"
				listEditable="false" multipleUsers="false" />
		</formEntity>
		<formEntity id="7" name="title" systemType="string"
			required="false" system="false">
			<children />
			<format type="textInput" viewingType="textInput"></format>
			<graphic hidden="false" readOnly="false" labelWidth="79"
				contentWidth="189" height="23" cellSize="1" fitWidth="false"
				verticalScrollPolicy="true" textAlign="left" fitToScreen="false"
				listEditable="false" multipleUsers="false" />
		</formEntity>
	</children>
	<mappingForms />
	<mappingServices />
	<layout type="grid_layout">
		<columns>
			<gridColumn size="586.0215298303115" labelSize="77.87661525984205" />
			<gridColumn size="586.0215298303115" labelSize="77.87661525984205" />
		</columns>
		<gridRow size="30">
			<gridCell size="586.0215298303115" span="2" rowSpan="1"
				gridColumnIndex="0" fieldId="7" />
		</gridRow>
		<gridRow size="30">
			<gridCell size="586.0215298303115" span="1" rowSpan="1"
				gridColumnIndex="0" fieldId="0" />
			<gridCell size="586.0215298303115" span="1" rowSpan="1"
				gridColumnIndex="1" fieldId="1" />
		</gridRow>
		<gridRow size="30">
			<gridCell size="586.0215298303115" span="1" rowSpan="1"
				gridColumnIndex="0" fieldId="2" />
			<gridCell size="586.0215298303115" span="1" rowSpan="1"
				gridColumnIndex="1" fieldId="3" />
		</gridRow>
		<gridRow size="30">
			<gridCell size="586.0215298303115" span="1" rowSpan="1"
				gridColumnIndex="0" fieldId="4" />
			<gridCell size="586.0215298303115" span="1" rowSpan="1"
				gridColumnIndex="1" fieldId="5" />
		</gridRow>
		<gridRow size="30">
			<gridCell size="586.0215298303115" span="2" rowSpan="3"
				gridColumnIndex="0" fieldId="6" />
		</gridRow>
		<gridRow size="30" />
		<gridRow size="30" />
	</layout>
	<graphic width="1184" height="392" currentEntityNum="8"
		currentMappingNum="0">
		<space />
	</graphic>
</form>');

CREATE TABLE Friends (
	objId varchar(50) NOT NULL,
	requestId varchar(50),
	requestName varchar(50),
	receiveId varchar(50),
	receiveName varchar(50),
	acceptStatus int,
	requestDate datetime,
	replyDate datetime,
	primary key (objId)
);

CREATE TABLE SeraUserDetail (
	userId varchar(50) NOT NULL,
	email varchar(50),
	birthday datetime,
	sex int,
	goal varchar(500),
	interests varchar(500),
	educations varchar(500),
	works varchar(500),
	twUserId varchar(50),
	twPassword varchar(100),
	fbUserId varchar(50),
	fbPassword varchar(100),
	nickName varchar(100),
	challengingTarget varchar(500),
	primary key (userId)
);


--Note Tabel
CREATE TABLE SeraNote (
	id varchar(50)NOT NULL,
	domainid varchar(50),
	workitemid varchar(50),
	masterrecordid varchar(50),
	creator varchar(50),
	createdtime datetime ,
	modifier varchar(50),
	modifiedtime datetime ,
	hit int,
	workSpaceId varchar(100),
	workSpaceType varchar(50),
	accessLevel varchar(1),
	accessValue varchar(4000),
	videoFileSize varchar(255),
	content text,
	videoFileName varchar(255),
	fileGroupId varchar(255),
	linkUrl varchar(255),
	imageFileGroupId varchar(255),
	videoYTId varchar(255),
	primary key (id)
);

INSERT INTO swpackage (id, categoryid, packageid, version, name, type, status, latestdeployedyn, creator, createdtime, modifier, modifiedtime, description) VALUES ('402880eb36b487f60136b48c4f920001', '40288afb1b25f00b011b25f3c7950001', 'pkg_e4c34f837ea64b1c994d4827d8a4bb51', 1, '세라노트', 'SINGLE', 'DEPLOYED', 'Y', 'admin', getdate(), 'admin', getdate(), '세라노트');

INSERT INTO swdomain (id, companyId, formid, formversion, formname, tblowner, tblname, keycolumn, titlefieldid, masterid, masterfieldid, systemdomainyn, publishmode) VALUES ('sera_note','Maninsoft', 'sera_note', 1, '세라노트', '', 'SeraNote', 'id', '5', NULL, NULL, 'Y', 'PUB_ALL');

INSERT INTO swdomainfield (id, domainid, formfieldid, formfieldpath, formfieldname, formfieldtype, tablecolname, reftblname, arrayyn, systemfieldyn, uniqueyn, disporder, tablewidth) VALUES ('sera_note_videoFileSize', 'sera_note', '8', NULL, 'videoFileSize', 'string', 'videoFileSize', NULL, 'N', 'N', NULL, -1, 0);
INSERT INTO swdomainfield (id, domainid, formfieldid, formfieldpath, formfieldname, formfieldtype, tablecolname, reftblname, arrayyn, systemfieldyn, uniqueyn, disporder, tablewidth) VALUES ('sera_note_content', 'sera_note', '5', NULL, 'content', 'text', 'content', NULL, 'N', 'N', NULL, 5, 0);
INSERT INTO swdomainfield (id, domainid, formfieldid, formfieldpath, formfieldname, formfieldtype, tablecolname, reftblname, arrayyn, systemfieldyn, uniqueyn, disporder, tablewidth) VALUES ('sera_note_videoFileName', 'sera_note', '7', NULL, 'videoFileName', 'string', 'videoFileName', NULL, 'N', 'N', NULL, -1, 0);
INSERT INTO swdomainfield (id, domainid, formfieldid, formfieldpath, formfieldname, formfieldtype, tablecolname, reftblname, arrayyn, systemfieldyn, uniqueyn, disporder, tablewidth) VALUES ('sera_note_file', 'sera_note', '9', NULL, 'fileGroup', 'string', 'fileGroupId', NULL, 'N', 'N', NULL, -1, 0);
INSERT INTO swdomainfield (id, domainid, formfieldid, formfieldpath, formfieldname, formfieldtype, tablecolname, reftblname, arrayyn, systemfieldyn, uniqueyn, disporder, tablewidth) VALUES ('sera_note_linkUrl', 'sera_note', '3', NULL, 'linkUrl', 'string', 'linkUrl', NULL, 'N', 'N', NULL, -1, 0);
INSERT INTO swdomainfield (id, domainid, formfieldid, formfieldpath, formfieldname, formfieldtype, tablecolname, reftblname, arrayyn, systemfieldyn, uniqueyn, disporder, tablewidth) VALUES ('sera_note_imageFile', 'sera_note', '10', NULL, 'imageFileGroup', 'string', 'imageFileGroupId', NULL, 'N', 'N', NULL, -1, 0);
INSERT INTO swdomainfield (id, domainid, formfieldid, formfieldpath, formfieldname, formfieldtype, tablecolname, reftblname, arrayyn, systemfieldyn, uniqueyn, disporder, tablewidth) VALUES ('sera_note_videoYTId', 'sera_note', '6', NULL, 'videoYTId', 'string', 'videoYTId', NULL, 'N', 'N', NULL, -1, 0);

INSERT INTO swform (id, packageid, formid, version, name, type, status, creator, createdtime, modifier, modifiedtime, content)
VALUES ('402880eb36b487f60136b48c4fc70002', 'pkg_e4c34f837ea64b1c994d4827d8a4bb51', 'sera_note', 1, '세라노트', 'SINGLE', 'DEPLOYED', 'admin', getdate(), 'admin', getdate(), '<form id="sera_note" version="1" name="세라노트">
	<children>
		<formEntity id="3" name="linkUrl" systemType="string"
			required="false" system="false">
			<children />
			<format type="textInput" viewingType="textInput"></format>
			<graphic hidden="false" readOnly="false" labelWidth="122"
				contentWidth="292" height="23" cellSize="1" fitWidth="false"
				verticalScrollPolicy="true" textAlign="left" fitToScreen="false"
				listEditable="false" multipleUsers="false" />
		</formEntity>
		<formEntity id="5" name="content" systemType="text"
			required="false" system="false">
			<children />
			<format type="richEditor" viewingType="richEditor"></format>
			<graphic hidden="false" readOnly="false" labelWidth="122"
				contentWidth="772" height="158" cellSize="1" fitWidth="false"
				verticalScrollPolicy="true" textAlign="left" fitToScreen="false"
				listEditable="false" multipleUsers="false" />
		</formEntity>
		<formEntity id="6" name="videoYTId" systemType="string"
			required="false" system="false">
			<children />
			<format type="textInput" viewingType="textInput"></format>
			<graphic hidden="false" readOnly="false" labelWidth="78"
				contentWidth="188" height="23" cellSize="1" fitWidth="false"
				verticalScrollPolicy="true" textAlign="left" fitToScreen="false"
				listEditable="false" multipleUsers="false" />
		</formEntity>
		<formEntity id="7" name="videoFileName" systemType="string"
			required="false" system="false">
			<children />
			<format type="textInput" viewingType="textInput"></format>
			<graphic hidden="false" readOnly="false" labelWidth="78"
				contentWidth="188" height="23" cellSize="1" fitWidth="false"
				verticalScrollPolicy="true" textAlign="left" fitToScreen="false"
				listEditable="false" multipleUsers="false" />
		</formEntity>
		<formEntity id="8" name="videoFileSize" systemType="string"
			required="false" system="false">
			<children />
			<format type="textInput" viewingType="textInput"></format>
			<graphic hidden="false" readOnly="false" labelWidth="78"
				contentWidth="188" height="23" cellSize="1" fitWidth="false"
				verticalScrollPolicy="true" textAlign="left" fitToScreen="false"
				listEditable="false" multipleUsers="false" />
		</formEntity>
		<formEntity id="9" name="fileGroup" systemType="string"
			required="false" system="false">
			<children />
			<format type="fileField" viewingType="fileField"></format>
			<graphic hidden="false" readOnly="false" labelWidth="79"
				contentWidth="274" height="23" cellSize="1" fitWidth="false"
				verticalScrollPolicy="true" textAlign="left" fitToScreen="false"
				listEditable="false" multipleUsers="false" />
		</formEntity>
		<formEntity id="10" name="imageFileGroup" systemType="string"
			required="false" system="false">
			<children />
			<format type="fileField" viewingType="fileField"></format>
			<graphic hidden="false" readOnly="false" labelWidth="79"
				contentWidth="274" height="23" cellSize="1" fitWidth="false"
				verticalScrollPolicy="true" textAlign="left" fitToScreen="false"
				listEditable="false" multipleUsers="false" />
		</formEntity>
	</children>
	<mappingForms />
	<mappingServices />
	<layout type="grid_layout">
		<columns>
			<gridColumn size="586.0001821408293" labelSize="77.8737783575853" />
			<gridColumn size="586.0001821408293" labelSize="77.8737783575853" />
		</columns>
		<gridRow size="105.5">
			<gridCell size="586.0001821408293" span="2" rowSpan="3"
				gridColumnIndex="0" fieldId="5" />
		</gridRow>
		<gridRow size="30" />
		<gridRow size="30" />
		<gridRow size="30">
			<gridCell size="586.0001821408293" span="1" rowSpan="1"
				gridColumnIndex="0" fieldId="3" />
			<gridCell size="586.0001821408293" span="1" rowSpan="1"
				gridColumnIndex="1" fieldId="" />
		</gridRow>
		<gridRow size="30">
			<gridCell size="586.0001821408293" span="1" rowSpan="1"
				gridColumnIndex="0" fieldId="6" />
			<gridCell size="586.0001821408293" span="1" rowSpan="1"
				gridColumnIndex="1" fieldId="7" />
		</gridRow>
		<gridRow size="30">
			<gridCell size="586.0001821408293" span="1" rowSpan="1"
				gridColumnIndex="0" fieldId="8" />
			<gridCell size="586.0001821408293" span="1" rowSpan="1"
				gridColumnIndex="1" fieldId="" />
		</gridRow>
		<gridRow size="30">
			<gridCell size="586.0001821408293" span="2" rowSpan="1"
				gridColumnIndex="0" fieldId="9" />
		</gridRow>
		<gridRow size="30">
			<gridCell size="586.0001821408293" span="2" rowSpan="1"
				gridColumnIndex="0" fieldId="10" />
		</gridRow>
	</layout>
	<graphic width="1184" height="475" currentEntityNum="11"
		currentMappingNum="0">
		<space />
	</graphic>
</form>');






















--MissionReport Tabel
CREATE TABLE MissionReport (
	id varchar(50)NOT NULL,
	domainid varchar(50),
	workitemid varchar(50),
	masterrecordid varchar(50),
	creator varchar(50),
	createdtime datetime ,
	modifier varchar(50),
	modifiedtime datetime ,
	hit int,
	workSpaceId varchar(100),
	workSpaceType varchar(50),
	accessLevel varchar(1),
	accessValue varchar(4000),
	videoFileSize varchar(255),
	starPoint varchar(255),
	content text,
	videoFileName varchar(255),
	fileGroup varchar(255),
	linkUrl varchar(255),
	imageFileGroup varchar(255),
	videoYTId varchar(255),
	primary key (id)
);

INSERT INTO swpackage (id, categoryid, packageid, version, name, type, status, latestdeployedyn, creator, createdtime, modifier, modifiedtime, description) VALUES ('402880eb36b5fe8c0136b6025fc50001', '40288afb1b25f00b011b25f3c7950001', 'pkg_8fc9ed30a64b467eb89fd35097cc6212', 1, '미션리포트', 'SINGLE', 'DEPLOYED', 'Y', 'admin', getdate(), 'admin', getdate(), '미션리포트');

INSERT INTO swdomain (id, companyId, formid, formversion, formname, tblowner, tblname, keycolumn, titlefieldid, masterid, masterfieldid, systemdomainyn, publishmode) VALUES ('mission_report','Maninsoft', 'mission_report', 1, '미션리포트', '', 'MissionReport', 'id', '0', NULL, NULL, 'Y', 'PUB_ALL');

INSERT INTO swdomainfield (id, domainid, formfieldid, formfieldpath, formfieldname, formfieldtype, tablecolname, reftblname, arrayyn, systemfieldyn, uniqueyn, disporder, tablewidth) VALUES ('sera_report_videoFileSize', 'mission_report', '5', NULL, 'videoFileSize', 'string', 'videoFileSize', NULL, 'N', 'N', NULL, -1, 0);
INSERT INTO swdomainfield (id, domainid, formfieldid, formfieldpath, formfieldname, formfieldtype, tablecolname, reftblname, arrayyn, systemfieldyn, uniqueyn, disporder, tablewidth) VALUES ('sera_report_starPoint', 'mission_report', '2', NULL, 'starPoint', 'string', 'starPoint', NULL, 'N', 'N', NULL, 2, 0);
INSERT INTO swdomainfield (id, domainid, formfieldid, formfieldpath, formfieldname, formfieldtype, tablecolname, reftblname, arrayyn, systemfieldyn, uniqueyn, disporder, tablewidth) VALUES ('sera_report_content', 'mission_report', '0', NULL, 'content', 'text', 'content', NULL, 'N', 'N', NULL, 4, 0);
INSERT INTO swdomainfield (id, domainid, formfieldid, formfieldpath, formfieldname, formfieldtype, tablecolname, reftblname, arrayyn, systemfieldyn, uniqueyn, disporder, tablewidth) VALUES ('sera_report_imageFileGroup', 'mission_report', '7', NULL, 'imageFileGroup', 'string', 'imageFileGroup', NULL, 'N', 'N', NULL, 5, 0);
INSERT INTO swdomainfield (id, domainid, formfieldid, formfieldpath, formfieldname, formfieldtype, tablecolname, reftblname, arrayyn, systemfieldyn, uniqueyn, disporder, tablewidth) VALUES ('sera_report_linkUrl', 'mission_report', '1', NULL, 'linkUrl', 'string', 'linkUrl', NULL, 'N', 'N', NULL, 3, 0);
INSERT INTO swdomainfield (id, domainid, formfieldid, formfieldpath, formfieldname, formfieldtype, tablecolname, reftblname, arrayyn, systemfieldyn, uniqueyn, disporder, tablewidth) VALUES ('sera_report_videoFileName', 'mission_report', '4', NULL, 'videoFileName', 'string', 'videoFileName', NULL, 'N', 'N', NULL, -1, 0);
INSERT INTO swdomainfield (id, domainid, formfieldid, formfieldpath, formfieldname, formfieldtype, tablecolname, reftblname, arrayyn, systemfieldyn, uniqueyn, disporder, tablewidth) VALUES ('sera_report_videoYTId', 'mission_report', '3', NULL, 'videoYTId', 'string', 'videoYTId', NULL, 'N', 'N', NULL, 1, 0);
INSERT INTO swdomainfield (id, domainid, formfieldid, formfieldpath, formfieldname, formfieldtype, tablecolname, reftblname, arrayyn, systemfieldyn, uniqueyn, disporder, tablewidth) VALUES ('sera_report_fileGroup', 'mission_report', '6', NULL, 'fileGroup', 'string', 'fileGroup', NULL, 'N', 'N', NULL, 6, 0);

INSERT INTO swform (id, packageid, formid, version, name, type, status, creator, createdtime, modifier, modifiedtime, content)
VALUES ('402880eb36b5fe8c0136b60260030002', 'pkg_8fc9ed30a64b467eb89fd35097cc6212', 'mission_report', 1, '미션리포트', 'SINGLE', 'DEPLOYED', 'admin', getdate(), 'admin', getdate(), '<form id="mission_report" version="1" name="미션리포트">
	<children>
		<formEntity id="0" name="content" systemType="text"
			required="false" system="false">
			<children />
			<format type="richEditor" viewingType="richEditor"></format>
			<graphic hidden="false" readOnly="false" labelWidth="124"
				contentWidth="777" height="122" cellSize="1" fitWidth="false"
				verticalScrollPolicy="true" textAlign="left" fitToScreen="false"
				listEditable="false" multipleUsers="false" />
		</formEntity>
		<formEntity id="1" name="linkUrl" systemType="string"
			required="false" system="false">
			<children />
			<format type="textInput" viewingType="textInput"></format>
			<graphic hidden="false" readOnly="false" labelWidth="124"
				contentWidth="295" height="23" cellSize="1" fitWidth="false"
				verticalScrollPolicy="true" textAlign="left" fitToScreen="false"
				listEditable="false" multipleUsers="false" />
		</formEntity>
		<formEntity id="2" name="starPoint" systemType="string"
			required="false" system="false">
			<children />
			<format type="textInput" viewingType="textInput"></format>
			<graphic hidden="false" readOnly="false" labelWidth="124"
				contentWidth="295" height="23" cellSize="1" fitWidth="false"
				verticalScrollPolicy="true" textAlign="left" fitToScreen="false"
				listEditable="false" multipleUsers="false" />
		</formEntity>
		<formEntity id="3" name="videoYTId" systemType="string"
			required="false" system="false">
			<children />
			<format type="textInput" viewingType="textInput"></format>
			<graphic hidden="false" readOnly="false" labelWidth="124"
				contentWidth="295" height="23" cellSize="1" fitWidth="false"
				verticalScrollPolicy="true" textAlign="left" fitToScreen="false"
				listEditable="false" multipleUsers="false" />
		</formEntity>
		<formEntity id="4" name="videoFileName" systemType="string"
			required="false" system="false">
			<children />
			<format type="textInput" viewingType="textInput"></format>
			<graphic hidden="false" readOnly="false" labelWidth="124"
				contentWidth="295" height="23" cellSize="1" fitWidth="false"
				verticalScrollPolicy="true" textAlign="left" fitToScreen="false"
				listEditable="false" multipleUsers="false" />
		</formEntity>
		<formEntity id="5" name="videoFileSize" systemType="string"
			required="false" system="false">
			<children />
			<format type="textInput" viewingType="textInput"></format>
			<graphic hidden="false" readOnly="false" labelWidth="124"
				contentWidth="295" height="23" cellSize="1" fitWidth="false"
				verticalScrollPolicy="true" textAlign="left" fitToScreen="false"
				listEditable="false" multipleUsers="false" />
		</formEntity>
		<formEntity id="6" name="fileGroup" systemType="string"
			required="false" system="false">
			<children />
			<format type="fileField" viewingType="fileField"></format>
			<graphic hidden="false" readOnly="false" labelWidth="124"
				contentWidth="427" height="23" cellSize="1" fitWidth="false"
				verticalScrollPolicy="true" textAlign="left" fitToScreen="false"
				listEditable="false" multipleUsers="false" />
		</formEntity>
		<formEntity id="7" name="imageFileGroup" systemType="string"
			required="false" system="false">
			<children />
			<format type="fileField" viewingType="fileField"></format>
			<graphic hidden="false" readOnly="false" labelWidth="124"
				contentWidth="427" height="23" cellSize="1" fitWidth="false"
				verticalScrollPolicy="true" textAlign="left" fitToScreen="false"
				listEditable="false" multipleUsers="false" />
		</formEntity>
	</children>
	<mappingForms />
	<mappingServices />
	<layout type="grid_layout">
		<columns>
			<gridColumn size="588.5558312655087" labelSize="78.21339950372209" />
			<gridColumn size="588.5558312655087" labelSize="78.21339950372209" />
		</columns>
		<gridRow size="129.5">
			<gridCell size="588.5558312655087" span="2" rowSpan="1"
				gridColumnIndex="0" fieldId="0" />
		</gridRow>
		<gridRow size="30">
			<gridCell size="588.5558312655087" span="1" rowSpan="1"
				gridColumnIndex="0" fieldId="1" />
			<gridCell size="588.5558312655087" span="1" rowSpan="1"
				gridColumnIndex="1" fieldId="2" />
		</gridRow>
		<gridRow size="30">
			<gridCell size="588.5558312655087" span="1" rowSpan="1"
				gridColumnIndex="0" fieldId="3" />
			<gridCell size="588.5558312655087" span="1" rowSpan="1"
				gridColumnIndex="1" fieldId="4" />
		</gridRow>
		<gridRow size="30">
			<gridCell size="588.5558312655087" span="1" rowSpan="1"
				gridColumnIndex="0" fieldId="5" />
			<gridCell size="588.5558312655087" span="1" rowSpan="1"
				gridColumnIndex="1" fieldId="" />
		</gridRow>
		<gridRow size="30">
			<gridCell size="588.5558312655087" span="2" rowSpan="1"
				gridColumnIndex="0" fieldId="6" />
		</gridRow>
		<gridRow size="30">
			<gridCell size="588.5558312655087" span="2" rowSpan="1"
				gridColumnIndex="0" fieldId="7" />
		</gridRow>
	</layout>
	<graphic width="1184" height="459" currentEntityNum="8"
		currentMappingNum="0">
		<space />
	</graphic>
</form>');



-------------------------------------------------------------------------------
ALTER TABLE coursedetail add createDate datetime;
ALTER TABLE coursedetail add coursePoint int;
ALTER TABLE SeraUserDetail add nickName varchar(100);
ALTER TABLE SeraUserDetail add challengingTarget varchar(500);

ALTER TABLE SWOrgGroupMember add creator varchar(50);
ALTER TABLE SWOrgGroupMember add createdtime datetime;
ALTER TABLE SWOrgGroupMember add modifier varchar(50);
ALTER TABLE SWOrgGroupMember add modifiedtime datetime;

CREATE TABLE CourseReview (
	objId varchar(50) NOT NULL,
	courseId varchar(50),
	content varchar(4000),
	startPoint decimal(18,1),
	creator	varchar(50),
	createdtime datetime,
	modifier varchar(50),
	modifiedtime datetime,
	primary key(objId)
);

CREATE TABLE CourseTeam (
	objId varchar(50) NOT NULL,
	name varchar(100),
	courseId varchar(50),
	description varchar(4000),
	accessPolicy int,
	memberSize int,
	startDate datetime,
	endDate datetime,
	primary key(objId)
);

CREATE TABLE CourseTeamUser (
	objId varchar(50) NOT NULL,
	userId varchar(50) NOT NULL,
	memberSeq int,
	primary key(objId, userId)
);