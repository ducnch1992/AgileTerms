import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import Loading from "../../components/shared/Loading";
import TermContributionForm from "../../components/TermContributionForm/TermContributionForm";
import service from "../../services/Service";
export default function TermContribution() {
  const [dataList, setDataList] = useState({});
  const navigate = useNavigate();
  const [activatedUser, setActivatedUser] = useState(false);

  useEffect(() => {
    if (localStorage.getItem("token") == null) {
      setTimeout(() => {
        navigate("/login?return=/term-contribution");
      }, 500);
    } else if (
      localStorage.getItem("activate-status") === "false"
    ) {
      setTimeout(() => {
        navigate("/not-activated");
      }, 500);
    } else {
      setActivatedUser(true);
  
      const fetchData = async () => {
        const getTopicList = await service.getAll("topics");

        setDataList({
          topicList: getTopicList.data.map((topic) => {
            return { value: topic.id, label: topic.name, color: topic.color };
          }),
        });
      };
      
      fetchData();
    }
  }, [navigate]);

  return activatedUser ? (
    <section className="term-contribution-page container bg-light px-5">
      <h4 className="term-contribution-page__header py-3 font-weight-bold ">
        Contribute term
      </h4>
      <TermContributionForm topicList={dataList.topicList} />
    </section>
  ) : (
    <Loading />
  );
}
