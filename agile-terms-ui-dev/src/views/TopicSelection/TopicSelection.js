import React, { useState, useEffect } from "react";
import TopicSelect from "../../components/TermContribution/TopicSelect";
import service from "../../services/Service";
export default function TopicSelection() {
  const [dataList, setDataList] = useState({});
  useEffect(() => {
    service.getAll("topics").then((response) => {
      setDataList(
        response.data.map((topic) => {
          return { value: topic.id, label: topic.name, color: topic.color };
        })
      );
    });
  }, []);
  return <TopicSelect dataList={dataList} />;
}
