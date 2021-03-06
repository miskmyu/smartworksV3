CREATE TABLE UcityWorkList (
	objId varchar(50) NOT NULL,	
	prcInstId varchar(50),
	packageId varchar(100),
	status varchar(100),
	title varchar(200),
	runningTaskId varchar(50),
	runningTaskName varchar(200),
	serviceName varchar(200),
	eventId varchar(50),
	eventTime timestamp,
	eventName varchar(200),
	type varchar(100),
	externalDisplay varchar(200),
	isSms varchar(10),
	eventPlace varchar(100),	
	creator	varchar(50),
	createdtime timestamp,
	modifier varchar(50),
	modifiedtime timestamp,
	primary key (objId)
);
