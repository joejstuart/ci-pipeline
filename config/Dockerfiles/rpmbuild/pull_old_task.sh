#!/bin/bash

set -xe

# Ensure we have required variable
if [ -z "${PROVIDED_KOJI_TASKID}" ]; then echo "No task id variable provided" ; exit 1 ; fi

CURRENTDIR=$(pwd)
if [ ${CURRENTDIR} == "/" ] ; then
    cd /home
    CURRENTDIR=/home
fi

LOGDIR=${CURRENTDIR}/logs
rm -rf ${LOGDIR}/*
mkdir ${LOGDIR}

# Create trap function to archive as many of the variables as we have defined
function archive_variables {
    set +e
    cat << EOF > ${LOGDIR}/job.props
koji_task_id=${PROVIDED_KOJI_TASKID}
fed_repo=${PACKAGE}
fed_branch=${FED_BRANCH}
branch=${BRANCH}
fed_rev=kojitask-${PROVIDED_KOJI_TASKID}
nvr=${NVR}
original_spec_nvr=${NVR}
rpm_repo=${RPMDIR}
EOF
rm -rf somewhere
}
trap archive_variables EXIT SIGHUP SIGINT SIGTERM

mkdir somewhere
pushd somewhere
# Download koji build so we can archive it
koji download-task --arch=x86_64 --arch=src ${PROVIDED_KOJI_TASKID} --logs
createrepo .
PACKAGE=$(rpm --queryformat "%{NAME}\n" -qp *.src.rpm)
NVR=$(rpm --queryformat "%{NAME} %{VERSION} %{RELEASE}\n" -qp *.src.rpm)
TARGET_BRANCH=$(koji taskinfo --verbose ${PROVIDED_KOJI_TASKID} | grep Target | awk '{print $3}')
popd

RPMDIR=${CURRENTDIR}/${PACKAGE}_repo
rm -rf ${RPMDIR}
mkdir -p ${RPMDIR}

mv somewhere/* ${RPMDIR}/

# build target is rawhide or f**-candidate
if [ $TARGET_BRANCH == "rawhide" ]; then
    FED_BRANCH=$TARGET_BRANCH
    BRANCH="master"
else
    BRANCH="$(echo $TARGET_BRANCH |sed -E "s/-.*//")"
    FED_BRANCH=$BRANCH
fi

# Let's archive the logs too
cp ${RPMDIR}/*.log ${LOGDIR}/

archive_variables
